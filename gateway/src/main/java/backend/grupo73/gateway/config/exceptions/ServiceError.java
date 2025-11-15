package backend.grupo73.gateway.config.exceptions;

import lombok.Getter;

@Getter
public class ServiceError extends RuntimeException {

    private final int httpCode;

    public ServiceError(String message, Integer httpcode) {
        super(message);
        this.httpCode = httpcode;
    }

}
