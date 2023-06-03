package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.exception.RequestException;
import br.uefs.pbl_redes_3.model.JointAccountModel;
import br.uefs.pbl_redes_3.repository.ClientRepository;
import br.uefs.pbl_redes_3.repository.JointAccountRepository;
import br.uefs.pbl_redes_3.request.JointAccountRequest;
import br.uefs.pbl_redes_3.response.JointAccountResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class JointAccountService {
    private final JointAccountRepository jointAccountRepository;
    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    public JointAccountService(JointAccountRepository jointAccountRepository, ClientRepository clientRepository, ModelMapper modelMapper) {
        this.jointAccountRepository = jointAccountRepository;
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
    }

    public JointAccountResponse create(JointAccountRequest request){
        if(clientRepository.contains(c -> c.getId().equals(request.getClientId1())) && clientRepository.contains(c -> c.getId().equals(request.getClientId2()))){
            JointAccountModel jointAccount = modelMapper.map(request,JointAccountModel.class);
            return modelMapper.map(jointAccountRepository.save(jointAccount),JointAccountResponse.class);
        }
        throw new RequestException(HttpStatus.NOT_FOUND, "CLIENT");

    }



}
