package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.model.ClientModel;
import br.uefs.pbl_redes_3.repository.ClientRepository;
import br.uefs.pbl_redes_3.request.ClientRequest;
import br.uefs.pbl_redes_3.response.ClientResponse;
import br.uefs.pbl_redes_3.utils.Mapper;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private final ClientRepository clientRapository;

    public ClientService(final ClientRepository clientRapository) {
        this.clientRapository = clientRapository;
    }

    public ClientResponse create(ClientRequest request){
        ClientModel client = Mapper.map(request,ClientModel.class);
        if(clientRapository.contains(c -> c.getEmail().equals(client.getEmail()))){
            if(clientRapository.contains(c -> c.getCpf() == client.getCpf())){
                return Mapper.map(clientRapository.save(client), ClientResponse.class);
            }
        }
        return null;
    }
}
