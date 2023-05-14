package br.uefs.pbl_redes_3.controller;

import br.uefs.pbl_redes_3.Request.LoginRequest;
import br.uefs.pbl_redes_3.model.TokenResponse;
import br.uefs.pbl_redes_3.service.TokenService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    private final TokenService tokenService;

    public LoginController(final TokenService tokenService) {
        this.tokenService = tokenService;
    }


    public TokenResponse create(@RequestBody final LoginRequest request){
        return null;
    }
}
