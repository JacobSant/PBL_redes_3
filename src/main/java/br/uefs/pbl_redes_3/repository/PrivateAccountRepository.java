package br.uefs.pbl_redes_3.repository;

import br.uefs.pbl_redes_3.model.PrivateAccountModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class PrivateAccountRepository implements IRepository<PrivateAccountModel, UUID> {
    List<PrivateAccountModel> privateAccounts = new ArrayList<>();

    @Override
    public PrivateAccountModel save(PrivateAccountModel model) {
        model.setId(UUID.fromString(model.getAccountNumber() + UUID.randomUUID().toString()));
        privateAccounts.add(model);
        return model;
    }

    @Override
    public boolean deleteIf(Predicate<PrivateAccountModel> predicate) {
        return privateAccounts.removeIf(predicate);
    }

    public boolean deleteById(UUID uuid){
        return privateAccounts.removeIf(acc -> acc.getId() == uuid);
    }

    @Override
    public Optional<PrivateAccountModel> update(PrivateAccountModel model) {
        Optional<PrivateAccountModel> result = privateAccounts.stream().filter(acc -> acc.getAccountNumber() == model.getAccountNumber()).findFirst();
        Optional<PrivateAccountModel> updatedAccount = Optional.empty();
        if(result.isPresent()){
            model.setId(result.get().getId());
            updatedAccount = Optional.of(model);
            privateAccounts.removeIf(acc -> acc.getId() == model.getId());
            privateAccounts.add(model);
        }
        return updatedAccount;
    }

    @Override
    public boolean contains(Predicate<PrivateAccountModel> predicate) {
        return privateAccounts.stream().anyMatch(predicate);
    }

    @Override
    public Optional<PrivateAccountModel> findById(UUID uuid) {
        return privateAccounts.stream().filter(acc -> acc.getBankId() == uuid).findFirst();
    }

    @Override
    public List<PrivateAccountModel> findAll(Predicate<PrivateAccountModel> predicate) {
        return privateAccounts.stream().filter(predicate).collect(Collectors.toList());
    }
}
