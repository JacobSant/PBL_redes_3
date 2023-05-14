package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.Request.ClientRequest;
import br.uefs.pbl_redes_3.Response.ClientResponse;
import br.uefs.pbl_redes_3.model.ClientModel;
import br.uefs.pbl_redes_3.repository.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private final ClientRepository clientRapository;

    public ClientService(final ClientRepository clientRapository) {
        this.clientRapository = clientRapository;
    }

    public ClientModel create(ClientModel client){
        if(clientRapository.contains(c -> c.getEmail().equals(client.getEmail()))){
            if(clientRapository.contains(c -> c.getCpf() == client.getCpf())){
                return clientRapository.save(client);
            }
        }
        return null;
    }
}
