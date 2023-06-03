package br.uefs.pbl_redes_3.controller;

import br.uefs.pbl_redes_3.request.LoginRequest;
import br.uefs.pbl_redes_3.response.LoginResponse;
import br.uefs.pbl_redes_3.service.TokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final TokenService tokenService;

    public LoginController(final TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/private_account")
    public LoginResponse createPrivateAccountToken(@RequestBody final LoginRequest request){
        return tokenService.createPrivateAccountToken(request);
    }

    @PostMapping("/joint_account")
    public LoginResponse createJointAccountToken(@RequestBody final LoginRequest request){
        return tokenService.createJointAccountToken(request);
    }
}
