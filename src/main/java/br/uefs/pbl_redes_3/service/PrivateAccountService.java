package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.model.PrivateAccountModel;
import br.uefs.pbl_redes_3.repository.PrivateAccountRepository;
import br.uefs.pbl_redes_3.request.PrivateAccountRequest;
import br.uefs.pbl_redes_3.response.PrivateAccountResponse;
import br.uefs.pbl_redes_3.utils.Mapper;
import org.springframework.stereotype.Service;

@Service
public class PrivateAccountService {
    private final PrivateAccountRepository privateAccountRepository;

    public PrivateAccountService(PrivateAccountRepository privateAccountRepository) {
        this.privateAccountRepository = privateAccountRepository;
    }

    public PrivateAccountResponse create(PrivateAccountRequest request) {
        PrivateAccountModel privateAccount = Mapper.map(request,PrivateAccountModel.class);
        return Mapper.map(privateAccountRepository.save(privateAccount),PrivateAccountResponse.class);
    }
}
