package br.uefs.pbl_redes_3.repository;

import br.uefs.pbl_redes_3.model.TransfersModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class PrivateTransferRepository implements IRepository<TransfersModel, UUID> {
    List<TransfersModel> transfers = new ArrayList<>();
    @Override
    public TransfersModel save(TransfersModel model) {
        model.setId(UUID.nameUUIDFromBytes((model.getDate() + UUID.randomUUID().toString()).getBytes()));
        transfers.add(model);
        return model;
    }

    @Override
    public boolean deleteIf(Predicate<TransfersModel> predicate) {
        return transfers.removeIf(predicate);
    }

    @Override
    public Optional<TransfersModel> update(TransfersModel model) {
        return Optional.empty();
    }

    @Override
    public boolean contains(Predicate<TransfersModel> predicate) {
        return false;
    }

    @Override
    public Optional<TransfersModel> findById(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public List<TransfersModel> findAll(Predicate<TransfersModel> predicate) {
        return transfers.stream().filter(predicate).collect(Collectors.toList());
    }
}
