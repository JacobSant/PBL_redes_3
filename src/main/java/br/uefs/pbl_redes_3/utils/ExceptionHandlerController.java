package br.uefs.pbl_redes_3.utils;

import br.uefs.pbl_redes_3.exception.RegisterException;
import com.google.gson.JsonObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = {RegisterException.class})
    public ResponseEntity<String> handleRegisterException(RegisterException e) {
        JsonObject body = new JsonObject();
        body.addProperty("reason",e.getReason());
        return ResponseEntity.status(e.getStatus()).body(body.toString());
    }
}
