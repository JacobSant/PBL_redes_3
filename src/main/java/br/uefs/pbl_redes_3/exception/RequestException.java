package br.uefs.pbl_redes_3.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class RequestException extends RuntimeException {
    private HttpStatus status;
    private String reason;

    public RequestException(HttpStatus status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    public RequestException(HttpStatus status) {
        this.status = status;
        this.reason = "";
    }
}
