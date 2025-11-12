package utnfc.isi.back.unihelp.gateway.filter;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Rate limit didáctico en memoria (NO para prod):
 * - ventana deslizante de 60s
 * - 20 req/min por IP para /api/horarios/**
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryRateLimitFilter implements GlobalFilter, Ordered {

    private static final int LIMIT = 20;
    private static final long WINDOW_MS = 60_000;

    private static class Counter {
        AtomicInteger count = new AtomicInteger(0);
        volatile long windowStart = Instant.now().toEpochMilli();
    }

    private final Map<String, Counter> buckets = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        ServerHttpRequest req = exchange.getRequest();
        String path = req.getURI().getPath();
        if (!path.startsWith("/api/v1/**")) {
            return chain.filter(exchange); // limitar solo el servicio costoso
        }

        String ip = req.getHeaders().getFirst("X-Forwarded-For");
        if (ip == null || ip.isBlank()) {
            ip = req.getRemoteAddress() != null ? req.getRemoteAddress().getAddress().getHostAddress() : "unknown";
        }

        Counter c = buckets.computeIfAbsent(ip, k -> new Counter());
        long now = Instant.now().toEpochMilli();
        synchronized (c) {
            if (now - c.windowStart > WINDOW_MS) {
                c.windowStart = now;
                c.count.set(0);
            }
            int current = c.count.incrementAndGet();
            if (current > LIMIT) {
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                return exchange.getResponse().setComplete();
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() { return 10; }
}

