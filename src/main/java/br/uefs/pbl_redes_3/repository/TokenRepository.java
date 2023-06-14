package br.uefs.pbl_redes_3.repository;

import br.uefs.pbl_redes_3.model.ClientModel;
import br.uefs.pbl_redes_3.model.TokenModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TokenRepository {
    List<TokenModel> tokens = new ArrayList<>();

    public TokenModel save(TokenModel model) {
        if(exists(model.getAccountId(),model.getClientId())){
            tokens.removeIf(t -> t.getAccountId().equals(model.getAccountId()) && t.getClientId().equals(model.getClientId()));
        }
        tokens.add(model);
        return model;
    }

    public boolean exists(UUID accountId, UUID clientId){
        return tokens.stream().anyMatch(t -> t.getAccountId().equals(accountId) && t.getClientId().equals(clientId));
    }

    public boolean exists(UUID uuid){
        return tokens.stream().anyMatch(t -> t.getAccountId() == uuid);
    }

    public Optional<TokenModel> update(ClientModel client){
        Optional<TokenModel> token = tokens.stream().filter(t -> t.getClientId() == client.getId()).findFirst();
        if(token.isPresent()){
            String newToken = UUID.nameUUIDFromBytes((client.getName() + client.getEmail() + UUID.randomUUID()).getBytes()).toString();
            token.get().setToken(newToken);
            return token;
        }else{
            return Optional.empty();
        }
    }

    public Optional<TokenModel> findByToken(String token){
        return tokens.stream().filter(t -> t.getToken().equals(token)).findFirst();
    }
}
