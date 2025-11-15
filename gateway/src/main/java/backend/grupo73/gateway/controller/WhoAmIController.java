package backend.grupo73.gateway.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class WhoAmIController {

    @GetMapping("/gateway/whoami")
    public Mono<Map<String, Object>> whoami() {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication())
                .map(WhoAmIController::toInfo);
    }

    private static Map<String, Object> toInfo(Authentication auth) {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("authenticated", auth.isAuthenticated());
        out.put("authorities", auth.getAuthorities());

        Object p = auth.getPrincipal();

        if (p instanceof OidcUser u) {
            out.put("type", "OIDC");
            out.put("sub", u.getSubject());
            out.put("name", u.getFullName());
            out.put("preferred_username", u.getPreferredUsername());
            out.put("email", u.getEmail());
            out.put("claims", u.getClaims());
            return out;
        }

        if (p instanceof Jwt jwt) {
            out.put("type", "JWT");
            out.put("sub", jwt.getSubject());
            out.put("issuer", String.valueOf(jwt.getIssuer()));
            out.put("expires_at", String.valueOf(jwt.getExpiresAt()));
            out.put("claims", jwt.getClaims());
            return out;
        }

        out.put("type", p == null ? "null" : p.getClass().getName());
        out.put("principal", String.valueOf(p));
        return out;
    }
}
