package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.model.ClientModel;
import br.uefs.pbl_redes_3.repository.ClientRepository;
import br.uefs.pbl_redes_3.request.ClientRequest;
import br.uefs.pbl_redes_3.response.ClientResponse;
import br.uefs.pbl_redes_3.utils.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private final ClientRepository clientRapository;
    private final ModelMapper modelMapper;

    public ClientService(final ClientRepository clientRapository,
                         final ModelMapper modelMapper) {
        this.clientRapository = clientRapository;
        this.modelMapper = modelMapper;
    }

    public ClientResponse create(ClientRequest request){
        ClientModel client = modelMapper.map(request,ClientModel.class);
        if(!clientRapository.contains(c -> c.getEmail().equals(client.getEmail()))){
            if(!clientRapository.contains(c -> c.getCpf() == client.getCpf())){
                return modelMapper.map(clientRapository.save(client), ClientResponse.class);
            }
        }
        return null;
    }

    public ClientResponse findByEmail(String email){
        return modelMapper.map(clientRapository.findByEmail(email), ClientResponse.class);
    }
}
