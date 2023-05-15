package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.model.PrivateAccountModel;
import br.uefs.pbl_redes_3.repository.PrivateAccountRepository;
import br.uefs.pbl_redes_3.request.PrivateAccountRequest;
import br.uefs.pbl_redes_3.response.PrivateAccountResponse;
import br.uefs.pbl_redes_3.utils.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class PrivateAccountService {
    private final PrivateAccountRepository privateAccountRepository;
    private final ModelMapper modelMapper;

    public PrivateAccountService(final PrivateAccountRepository privateAccountRepository,
                                 final ModelMapper modelMapper) {
        this.privateAccountRepository = privateAccountRepository;
        this.modelMapper = modelMapper;
    }

    public PrivateAccountResponse create(PrivateAccountRequest request) {
        PrivateAccountModel privateAccount = modelMapper.map(request,PrivateAccountModel.class);
        return modelMapper.map(privateAccountRepository.save(privateAccount),PrivateAccountResponse.class);
    }
}
