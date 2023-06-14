package br.uefs.pbl_redes_3.repository;

import br.uefs.pbl_redes_3.model.DepositModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class PrivateDepositRepository implements IRepository<DepositModel, UUID> {
    private final List<DepositModel> deposits = new ArrayList<>();

    @Override
    public DepositModel save(DepositModel model) {
        model.setId(UUID.nameUUIDFromBytes((model.getDate() + UUID.randomUUID().toString()).getBytes()));
        deposits.add(model);
        return model;
    }

    @Override
    public boolean deleteIf(Predicate<DepositModel> predicate) {
        return deposits.removeIf(predicate);
    }

    @Override
    public Optional<DepositModel> update(DepositModel model) {
        return null;
    }

    @Override
    public boolean contains(Predicate<DepositModel> predicate) {
        return false;
    }

    @Override
    public Optional<DepositModel> findById(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public List<DepositModel> findAll(Predicate<DepositModel> predicate) {
        return deposits.stream().filter(predicate).collect(Collectors.toList());
    }
}
