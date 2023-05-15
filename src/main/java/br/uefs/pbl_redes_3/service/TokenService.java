package br.uefs.pbl_redes_3.service;
import br.uefs.pbl_redes_3.repository.TokenRepository;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;


    public TokenService(final TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }


}
