package br.uefs.pbl_redes_3.service;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final TokenService tokenService;


    public TokenService(final TokenService tokenService) {
        this.tokenService = tokenService;
    }


}
