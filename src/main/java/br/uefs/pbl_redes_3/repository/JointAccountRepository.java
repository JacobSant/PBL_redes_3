package br.uefs.pbl_redes_3.repository;

import br.uefs.pbl_redes_3.model.JointAccountModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class JointAccountRepository implements IRepository<JointAccountModel, UUID> {
    List<JointAccountModel> jointAccounts = new ArrayList<>();

    @Override
    public JointAccountModel save(JointAccountModel model) {
        model.setId(UUID.nameUUIDFromBytes((model.getAccountNumber() + UUID.randomUUID().toString()).getBytes()));
        jointAccounts.add(model);
        return model;
    }

    @Override
    public boolean deleteIf(Predicate<JointAccountModel> predicate) {
        return jointAccounts.removeIf(predicate);
    }

    public boolean deleteById(UUID uuid) {
        return jointAccounts.removeIf(acc -> acc.getId() == uuid);
    }

    @Override
    public Optional<JointAccountModel> update(JointAccountModel model) {
        Optional<JointAccountModel> result = jointAccounts.stream().filter(acc -> acc.getAccountNumber() == model.getAccountNumber()).findFirst();
        Optional<JointAccountModel> updatedAccount = Optional.empty();
        if (result.isPresent()) {
            model.setId(result.get().getId());
            updatedAccount = Optional.of(model);
            jointAccounts.removeIf(acc -> acc.getId() == model.getId());
            jointAccounts.add(model);
        }
        return updatedAccount;
    }

    @Override
    public boolean contains(Predicate<JointAccountModel> predicate) {
        return jointAccounts.stream().anyMatch(predicate);
    }

    @Override
    public Optional<JointAccountModel> findById(UUID uuid) {
        return jointAccounts.stream().filter(acc -> acc.getId().equals(uuid)).findFirst();
    }

    @Override
    public List<JointAccountModel> findAll(Predicate<JointAccountModel> predicate) {
        return jointAccounts.stream().filter(predicate).collect(Collectors.toList());
    }

    public Optional<JointAccountModel> findByAccountNumber(int accountNumber) {
        return jointAccounts.stream().filter(acc -> acc.getAccountNumber() == accountNumber).findFirst();
    }

    public Optional<JointAccountModel> findByClientId(UUID clientId) {
        return jointAccounts.stream().filter(acc -> acc.getClientId1().equals(clientId) || acc.getClientId2().equals(clientId)).findFirst();
    }
}