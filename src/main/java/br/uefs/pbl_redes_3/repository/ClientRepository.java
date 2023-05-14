package br.uefs.pbl_redes_3.repository;

import br.uefs.pbl_redes_3.model.ClientModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class ClientRepository implements IRepository<ClientModel, UUID> {
    List<ClientModel> clients = new ArrayList<>();
    @Override
    public ClientModel save(ClientModel model) {
        model.setId(UUID.fromString(model.getName() + model.getEmail()));
        clients.add(model);
        return model;
    }

    @Override
    public boolean deleteIf(Predicate<ClientModel> predicate) {
        return clients.removeIf(predicate);
    }

    public boolean deleteById(UUID uuid){
        return clients.removeIf(c -> c.getId() == uuid);
    }

    @Override
    public Optional<ClientModel> update(ClientModel model) {
        Optional<ClientModel> result = clients.stream().filter(acc -> acc.getEmail() == model.getEmail()).findFirst();
        Optional<ClientModel> updatedAccount = Optional.empty();
        if(result.isPresent()){
            model.setId(result.get().getId());
            updatedAccount = Optional.of(model);
            clients.removeIf(c -> c.getId() == model.getId());
            clients.add(model);
        }
        return updatedAccount;
    }

    @Override
    public boolean contains(Predicate<ClientModel> predicate) {
        return clients.stream().anyMatch(predicate);
    }

    @Override
    public Optional<ClientModel> findById(UUID uuid) {
        return clients.stream().filter(c -> c.getId() == uuid).findFirst();
    }

    @Override
    public List<ClientModel> findAll(Predicate<ClientModel> predicate) {
        return clients.stream().filter(predicate).collect(Collectors.toList());
    }
}
