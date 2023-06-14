package br.uefs.pbl_redes_3.repository;

import br.uefs.pbl_redes_3.model.PaymentsModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class PrivatePaymentRepository implements IRepository<PaymentsModel, UUID> {
    List<PaymentsModel> payments = new ArrayList<>();
    @Override
    public PaymentsModel save(PaymentsModel model) {
        model.setId(UUID.nameUUIDFromBytes((model.getDate() + UUID.randomUUID().toString()).getBytes()));
        payments.add(model);
        return model;
    }

    @Override
    public boolean deleteIf(Predicate<PaymentsModel> predicate) {
        return payments.removeIf(predicate);
    }

    @Override
    public Optional<PaymentsModel> update(PaymentsModel model) {
        return Optional.empty();
    }

    @Override
    public boolean contains(Predicate<PaymentsModel> predicate) {
        return false;
    }

    @Override
    public Optional<PaymentsModel> findById(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public List<PaymentsModel> findAll(Predicate<PaymentsModel> predicate) {
        return payments.stream().filter(predicate).collect(Collectors.toList());
    }
}
