package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.exception.RegisterException;
import br.uefs.pbl_redes_3.model.PrivateAccountModel;
import br.uefs.pbl_redes_3.repository.ClientRepository;
import br.uefs.pbl_redes_3.repository.PrivateAccountRepository;
import br.uefs.pbl_redes_3.request.PrivateAccountRequest;
import br.uefs.pbl_redes_3.response.PrivateAccountResponse;
import br.uefs.pbl_redes_3.utils.NumberAccountGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PrivateAccountService {
    private final PrivateAccountRepository privateAccountRepository;
    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;
    private final NumberAccountGenerator numberAccountGenerator;

    public PrivateAccountService(final PrivateAccountRepository privateAccountRepository,
                                 final ModelMapper modelMapper,
                                 final ClientRepository clientRepository,
                                 final NumberAccountGenerator numberAccountGenerator) {
        this.privateAccountRepository = privateAccountRepository;
        this.modelMapper = modelMapper;
        this.clientRepository = clientRepository;
        this.numberAccountGenerator = numberAccountGenerator;
    }

    public PrivateAccountResponse create(PrivateAccountRequest request) {
        UUID uuid = UUID.fromString(request.getClientId());

        if(clientRepository.contains(c -> c.getId().equals(uuid))){
            PrivateAccountModel privateAccount = modelMapper.map(request,PrivateAccountModel.class);
            privateAccount.setAccountNumber(numberAccountGenerator.create());
            return modelMapper.map(privateAccountRepository.save(privateAccount),PrivateAccountResponse.class);
        }
        throw new RegisterException(HttpStatus.NOT_FOUND, "CLIENT");
    }
}
