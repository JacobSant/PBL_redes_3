package br.uefs.pbl_redes_3.repository;

import br.uefs.pbl_redes_3.model.BankAccountModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@Repository
public class BankAccountRepository implements IRepository<BankAccountModel, UUID> {
    List<BankAccountModel> bankAccounts = new ArrayList<>();

    @Override
    public BankAccountModel save(BankAccountModel model) {
        bankAccounts.add(model);
        return model;
    }

    @Override
    public boolean deleteIf(Predicate<BankAccountModel> predicate) {
        return bankAccounts.removeIf(predicate);
    }

    @Override
    public BankAccountModel update(BankAccountModel model) {
        return null;
    }

    @Override
    public boolean contains(Predicate<BankAccountModel> predicate) {
        return bankAccounts.stream().anyMatch(predicate);
    }

    @Override
    public Optional<BankAccountModel> findById(UUID uuid) {
        return bankAccounts.stream().filter(bank -> bank.)
    }

    @Override
    public List<BankAccountModel> findAll(Predicate<BankAccountModel> predicate) {
        return null;
    }
}
