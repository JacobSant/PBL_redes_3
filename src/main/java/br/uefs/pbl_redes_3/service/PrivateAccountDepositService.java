package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.exception.RequestException;
import br.uefs.pbl_redes_3.model.PrivateAccountModel;
import br.uefs.pbl_redes_3.model.TokenModel;
import br.uefs.pbl_redes_3.repository.PrivateAccountRepository;
import br.uefs.pbl_redes_3.repository.TokenRepository;
import br.uefs.pbl_redes_3.request.DepositRequest;
import br.uefs.pbl_redes_3.response.DepositResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class PrivateAccountDepositService {
    private final TokenRepository tokenRepository;
    private final ModelMapper modelMapper;
    private final PrivateAccountRepository privateAccountRepository;

    public PrivateAccountDepositService(TokenRepository tokenRepository,
                                        PrivateAccountRepository privateAccountRepository,
                                        ModelMapper modelMapper) {
        this.tokenRepository = tokenRepository;
        this.privateAccountRepository = privateAccountRepository;
        this.modelMapper = modelMapper;
    }

    public DepositResponse create(DepositRequest request, String token) {
        Optional<TokenModel> tokenOptional = tokenRepository.findByToken(token);
        if(tokenOptional.isPresent()){
            TokenModel tokenModel = tokenOptional.get();
            Date currentDate = new Date();
            if(currentDate.getTime() < tokenModel.getExpiresAt()){
                Optional<PrivateAccountModel> privateAccountOptional = privateAccountRepository.findById(tokenModel.getAccountId());
                if(privateAccountOptional.isPresent()){
                    PrivateAccountModel privateAccount = privateAccountOptional.get();
                    privateAccount.setBalance(privateAccount.getBalance() + request.getValue());
                    return modelMapper.map(privateAccountRepository.update(privateAccount),DepositResponse.class);
                }else{
                    throw new RequestException(HttpStatus.NOT_FOUND,"PRIVATE ACCOUNT");
                }
            }else{
                throw new RequestException(HttpStatus.UNAUTHORIZED,"ACCESS TOKEN EXPIRED");
            }
        }else{
            throw new RequestException(HttpStatus.FORBIDDEN,"INVALID TOKEN");
        }
    }
}
