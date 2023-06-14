package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.exception.RequestException;
import br.uefs.pbl_redes_3.model.DepositModel;
import br.uefs.pbl_redes_3.model.JointAccountModel;
import br.uefs.pbl_redes_3.model.TokenModel;
import br.uefs.pbl_redes_3.repository.JointAccountRepository;
import br.uefs.pbl_redes_3.repository.JointDepositRepository;
import br.uefs.pbl_redes_3.repository.TokenRepository;
import br.uefs.pbl_redes_3.request.DepositRequest;
import br.uefs.pbl_redes_3.response.DepositResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class JointAccountDepositService {
    private final TokenRepository tokenRepository;
    private final ModelMapper modelMapper;
    private final JointAccountRepository jointAccountRepository;
    private final JointDepositRepository jointDepositRepository;

    public JointAccountDepositService(TokenRepository tokenRepository,
                                      JointAccountRepository jointAccountRepository,
                                      ModelMapper modelMapper, JointDepositRepository jointDepositRepository) {
        this.tokenRepository = tokenRepository;
        this.jointAccountRepository = jointAccountRepository;
        this.modelMapper = modelMapper;
        this.jointDepositRepository = jointDepositRepository;
    }

    public DepositResponse create(DepositRequest request, String token) {
        Optional<TokenModel> tokenOptional = tokenRepository.findByToken(token);
        if (tokenOptional.isPresent()) {
            TokenModel tokenModel = tokenOptional.get();
            Date currentDate = new Date();
            if (currentDate.getTime() < tokenModel.getExpiresAt()) {
                Optional<JointAccountModel> jointAccountOptional = jointAccountRepository.findById(tokenModel.getAccountId());
                if (jointAccountOptional.isPresent()) {
                    JointAccountModel jointAccount = jointAccountOptional.get();
                    jointAccount.setBalance(jointAccount.getBalance() + request.getValue());
                    DepositModel deposit = modelMapper.map(request, DepositModel.class);
                    deposit.setAccountId(jointAccount.getId());
                    deposit.setDate(LocalDateTime.now());
                    jointAccountRepository.update(jointAccount);
                    return modelMapper.map(jointDepositRepository.save(deposit), DepositResponse.class);
                } else {
                    throw new RequestException(HttpStatus.NOT_FOUND, "PRIVATE ACCOUNT");
                }
            } else {
                throw new RequestException(HttpStatus.UNAUTHORIZED, "ACCESS TOKEN EXPIRED");
            }
        } else {
            throw new RequestException(HttpStatus.FORBIDDEN, "INVALID TOKEN");
        }
    }
}
