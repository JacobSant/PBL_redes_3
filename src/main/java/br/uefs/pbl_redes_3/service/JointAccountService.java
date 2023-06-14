package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.exception.RequestException;
import br.uefs.pbl_redes_3.model.JointAccountModel;
import br.uefs.pbl_redes_3.repository.ClientRepository;
import br.uefs.pbl_redes_3.repository.JointAccountRepository;
import br.uefs.pbl_redes_3.request.JointAccountRequest;
import br.uefs.pbl_redes_3.response.JointAccountResponse;
import br.uefs.pbl_redes_3.utils.NumberAccountGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class JointAccountService {
    private final JointAccountRepository jointAccountRepository;
    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;
    private final NumberAccountGenerator numberAccountGenerator;

    public JointAccountService(final JointAccountRepository jointAccountRepository,
                               final ModelMapper modelMapper,
                               final ClientRepository clientRepository,
                               final NumberAccountGenerator numberAccountGenerator) {
        this.modelMapper = modelMapper;
        this.jointAccountRepository = jointAccountRepository;
        this.clientRepository = clientRepository;
        this.numberAccountGenerator = numberAccountGenerator;
    }

    public JointAccountResponse create(JointAccountRequest request) {
        UUID uuid1 = UUID.fromString(request.getClientId1());
        UUID uuid2 = UUID.fromString(request.getClientId2());
        if (clientRepository.contains(c -> c.getId().equals(uuid1)) && clientRepository.contains(c -> c.getId().equals(uuid2))) {
            JointAccountModel jointAccount = new JointAccountModel();
            jointAccount.setAccountNumber(numberAccountGenerator.create());
            jointAccount.setClientId1(uuid1);
            jointAccount.setClientId2(uuid2);
            return modelMapper.map(jointAccountRepository.save(jointAccount), JointAccountResponse.class);
        }

        throw new RequestException(HttpStatus.NOT_FOUND, "CLIENT");
    }

    public JointAccountResponse findByClientID(UUID clientId) {
        Optional<JointAccountModel> jointAccountOptional;
        jointAccountOptional = jointAccountRepository.findByClientId(clientId);

        if (jointAccountOptional.isPresent()) {
            return modelMapper.map(jointAccountOptional.get(), JointAccountResponse.class);
        }
        throw new RequestException(HttpStatus.NOT_FOUND, "PrivateAccount");
    }
}
