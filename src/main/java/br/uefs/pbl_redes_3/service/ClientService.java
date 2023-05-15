package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.exception.RegisterException;
import br.uefs.pbl_redes_3.model.ClientModel;
import br.uefs.pbl_redes_3.repository.ClientRepository;
import br.uefs.pbl_redes_3.request.ClientRequest;
import br.uefs.pbl_redes_3.response.ClientResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    public ClientService(final ClientRepository clientRepository,
                         final ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
    }

    public ClientResponse create(ClientRequest request) {
        ClientModel client = modelMapper.map(request, ClientModel.class);
        if (!clientRepository.contains(c -> c.getEmail().equals(client.getEmail()))) {
            if (!clientRepository.contains(c -> c.getCpf() == client.getCpf())) {
                return modelMapper.map(clientRepository.save(client), ClientResponse.class);
            } else {
                throw new RegisterException(HttpStatus.UNAUTHORIZED, "CPF");
            }
        } else {
            throw new RegisterException(HttpStatus.UNAUTHORIZED, "EMAIL");
        }
    }

    public ClientResponse findByEmail(String email) {
        return modelMapper.map(clientRepository.findByEmail(email), ClientResponse.class);
    }
}
