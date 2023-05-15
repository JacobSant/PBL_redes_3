package br.uefs.pbl_redes_3.controller;

import br.uefs.pbl_redes_3.request.LoginRequest;
import br.uefs.pbl_redes_3.response.LoginResponse;
import br.uefs.pbl_redes_3.service.TokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    private final TokenService tokenService;

    public LoginController(final TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public LoginResponse create(final LoginRequest request){
        return tokenService.create(request);
    }
}
