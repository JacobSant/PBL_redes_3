package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.model.JointAccountModel;
import br.uefs.pbl_redes_3.model.PrivateAccountModel;
import br.uefs.pbl_redes_3.repository.ClientRepository;
import br.uefs.pbl_redes_3.repository.JointAccountRepository;
import br.uefs.pbl_redes_3.request.JointAccountRequest;
import br.uefs.pbl_redes_3.request.PrivateAccountRequest;
import br.uefs.pbl_redes_3.response.JointAccountResponse;
import br.uefs.pbl_redes_3.response.PrivateAccountResponse;
import org.modelmapper.ModelMapper;

import java.sql.SQLOutput;

public class JointAccountService {
    private final JointAccountRepository jointAccountRepository;
    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    public JointAccountService(JointAccountRepository privateAccountRepository, ClientRepository clientRepository, ModelMapper modelMapper) {
        this.jointAccountRepository = privateAccountRepository;
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
    }

    public JointAccountResponse create(JointAccountRequest request){
        if(clientRepository.contains(c -> c.getId().equals(request.getClientId1())) && clientRepository.contains(c -> c.getId().equals(request.getClientId2()))){
            JointAccountModel jointAccount = modelMapper.map(request,JointAccountModel.class);
            return modelMapper.map(jointAccountRepository.save(jointAccount),JointAccountResponse.class);
        }
        return null;
    }



}
