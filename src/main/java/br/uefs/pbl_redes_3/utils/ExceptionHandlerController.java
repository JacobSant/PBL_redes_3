package br.uefs.pbl_redes_3.utils;

import br.uefs.pbl_redes_3.exception.RegisterException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = {RegisterException.class})
    public ResponseEntity<String> handleRegisterException(RegisterException e) {
        return ResponseEntity.status(e.getStatus()).body(e.getReason());
    }
}
