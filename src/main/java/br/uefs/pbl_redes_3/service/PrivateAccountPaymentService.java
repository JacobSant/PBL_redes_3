package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.exception.RequestException;
import br.uefs.pbl_redes_3.model.Bank;
import br.uefs.pbl_redes_3.model.PrivateAccountModel;
import br.uefs.pbl_redes_3.model.TokenModel;
import br.uefs.pbl_redes_3.repository.PrivateAccountRepository;
import br.uefs.pbl_redes_3.repository.TokenRepository;
import br.uefs.pbl_redes_3.request.PaymentRequest;
import br.uefs.pbl_redes_3.response.PaymentResponse;
import br.uefs.pbl_redes_3.utils.OtherBanks;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Optional;

public class PrivateAccountPaymentService {
    private final TokenRepository tokenRepository;
    private final ModelMapper modelMapper;
    @Value("${bank.id}")
    private int bankId;

    private final OtherBanks otherBanks;

    public PrivateAccountPaymentService(TokenRepository tokenRepository,
                                        ModelMapper modelMapper,
                                        OtherBanks otherBanks,
                                        PrivateAccountRepository privateAccountRepository) {
        this.tokenRepository = tokenRepository;
        this.modelMapper = modelMapper;
        this.otherBanks = otherBanks;
        this.privateAccountRepository = privateAccountRepository;
    }

    private final PrivateAccountRepository privateAccountRepository;

    public PaymentResponse create(PaymentRequest request, String token) {
        Optional<TokenModel> tokenOptional = tokenRepository.findByToken(token);
        if (tokenOptional.isPresent()) {
            TokenModel tokenModel = tokenOptional.get();
            Date currentDate = new Date();

            if (currentDate.getTime() < tokenModel.getExpiresAt()) {

                //
                if (request.getSourceBankId() == bankId && request.getDestinyBankId() == bankId) {
                    Optional<PrivateAccountModel> sourcePrivateAccountOptional = privateAccountRepository.findById(tokenModel.getAccountId());
                    Optional<PrivateAccountModel> destinyPrivateAccountOptional = privateAccountRepository.findByAccountNumber(request.getDestinyPrivateAccountNumber());
                    if (sourcePrivateAccountOptional.isPresent() && destinyPrivateAccountOptional.isPresent()) {
                        PrivateAccountModel sourcePrivateAccount = sourcePrivateAccountOptional.get();
                        PrivateAccountModel destinyPrivateAccount = destinyPrivateAccountOptional.get();
                        if (sourcePrivateAccount.getBalance() >= request.getValue()) {
                            sourcePrivateAccount.setBalance(sourcePrivateAccount.getBalance() - request.getValue());
                            destinyPrivateAccount.setBalance(destinyPrivateAccount.getBalance() + request.getValue());
                            privateAccountRepository.update(destinyPrivateAccount);
                            return modelMapper.map(privateAccountRepository.update(sourcePrivateAccount), PaymentResponse.class);
                        } else {
                            throw new RequestException(HttpStatus.UNAUTHORIZED, "INSUFFICIENT BALANCE");
                        }
                    } else {
                        throw new RequestException(HttpStatus.NOT_FOUND, "PRIVATE ACCOUNT");
                    }
                }
                //
                else if (request.getSourceBankId() != bankId && request.getDestinyBankId() == bankId) {
                    Optional<PrivateAccountModel> destinyPrivateAccountOptional = privateAccountRepository.findByAccountNumber(request.getDestinyPrivateAccountNumber());
                    if (destinyPrivateAccountOptional.isPresent()) {
                        PrivateAccountModel destinyPrivateAccount = destinyPrivateAccountOptional.get();
                        destinyPrivateAccount.setBalance(destinyPrivateAccount.getBalance() + request.getValue());

                        return modelMapper.map(privateAccountRepository.update(destinyPrivateAccount), PaymentResponse.class);
                    } else {
                        throw new RequestException(HttpStatus.NOT_FOUND, "PRIVATE ACCOUNT");
                    }
                } //
                else if (request.getSourceBankId() == bankId && request.getDestinyBankId() != bankId) {
                    Optional<PrivateAccountModel> sourcePrivateAccountOptional = privateAccountRepository.findById(tokenModel.getAccountId());
                    if(sourcePrivateAccountOptional.isPresent()){
                        RestTemplate httpRequest = new RestTemplate();
                        if (OtherBanks.getBanksReference().stream().anyMatch(b -> b.getId() == request.getDestinyBankId())) {
                            Bank bank = OtherBanks.getBanksReference().stream().filter(b -> b.getId() == request.getDestinyBankId()).findFirst().get();
                            return httpRequest.postForObject(bank.getIp() + "://" + bank.getPort() + "/bank_transfer", request, PaymentResponse.class);
                        } else {
                            throw new RequestException(HttpStatus.BAD_REQUEST, "INVALID BANK ID");
                        }
                    }
                }

            } else {
                throw new RequestException(HttpStatus.UNAUTHORIZED, "ACCESS TOKEN EXPIRED");
            }
        }
    }
}
