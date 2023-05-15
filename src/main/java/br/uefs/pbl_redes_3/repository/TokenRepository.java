package br.uefs.pbl_redes_3.repository;

import br.uefs.pbl_redes_3.model.TokenModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TokenRepository {
    List<TokenModel> tokens = new ArrayList<>();

    public TokenModel save(TokenModel model) {
        tokens.add(model);
        return model;
    }

}
