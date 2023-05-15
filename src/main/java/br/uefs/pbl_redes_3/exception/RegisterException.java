package br.uefs.pbl_redes_3.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Getter
@Setter
public class RegisterException extends RuntimeException{
    private HttpStatus status;
    private String reason;

    public RegisterException(HttpStatus status, String reason) {
        this.status = status;
        this.reason = reason;
    }
}
