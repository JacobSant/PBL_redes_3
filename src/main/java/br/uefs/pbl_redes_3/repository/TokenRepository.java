package br.uefs.pbl_redes_3.repository;

import br.uefs.pbl_redes_3.model.TokenModel;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Predicate;

@Repository
public class TokenRepository implements IRepository<TokenModel, UUID> {
    List<TokenModel> tokens = new ArrayList<>();
    @Override
    public TokenModel save(TokenModel model) {
        return null;
    }

    @Override
    public boolean deleteIf(Predicate<TokenModel> predicate) {
        return false;
    }

    @Override
    public Optional<TokenModel> update(TokenModel model) {
        return Optional.empty();
    }

    @Override
    public boolean contains(Predicate<TokenModel> predicate) {
        return false;
    }

    @Override
    public Optional<TokenModel> findById(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public List<TokenModel> findAll(Predicate<TokenModel> predicate) {
        return null;
    }

}
