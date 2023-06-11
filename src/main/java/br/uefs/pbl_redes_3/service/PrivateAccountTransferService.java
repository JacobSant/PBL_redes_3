package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.exception.RequestException;
import br.uefs.pbl_redes_3.model.Bank;
import br.uefs.pbl_redes_3.model.TokenModel;
import br.uefs.pbl_redes_3.repository.TokenRepository;
import br.uefs.pbl_redes_3.request.TransferRequest;
import br.uefs.pbl_redes_3.response.TransferResponse;
import br.uefs.pbl_redes_3.utils.Banks;
import br.uefs.pbl_redes_3.utils.RestTemplateResponseExceptionHandler;
import br.uefs.pbl_redes_3.utils.Synchronizer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Optional;

@Service
public class PrivateAccountTransferService {
    private final TokenRepository tokenRepository;
    private final Banks banks;
    private final Synchronizer synchronizer;

    public PrivateAccountTransferService(TokenRepository tokenRepository, Banks banks, Synchronizer synchronizer) {
        this.tokenRepository = tokenRepository;
        this.banks = banks;
        this.synchronizer = synchronizer;
    }

    public TransferResponse create(TransferRequest request, String token) {
        Optional<TokenModel> tokenOptional = tokenRepository.findByToken(token);
        if (tokenOptional.isPresent()) {
            TokenModel tokenModel = tokenOptional.get();
            Date currentDate = new Date();
            if (currentDate.getTime() < tokenModel.getExpiresAt()) {
                synchronizer.send(request.getSourcePrivateAccountNumber(), request.getSourceBankId());
                RestTemplate httpRequest = new RestTemplate();
                httpRequest.setErrorHandler(new RestTemplateResponseExceptionHandler());
                if (banks.getBanksReference().stream().anyMatch(b -> b.getId() == request.getSourceBankId())) {
                    Bank bank = banks.getBanksReference().stream()
                            .filter(b -> b.getId() == request.getSourceBankId()).findFirst().get();
                    String url ="http://"+ bank.getIp()  +":" +  bank.getPort()+ "/pass_transfer/private_account";
                    ResponseEntity<TransferResponse> response = httpRequest.postForEntity(url, request, TransferResponse.class);
                    TransferResponse result = response.getBody();
                    return result;
                }
            } else {
                throw new RequestException(HttpStatus.UNAUTHORIZED, "ACCESS TOKEN EXPIRED");
            }
        }

        throw new RequestException(HttpStatus.FORBIDDEN, "INVALID TOKEN");
    }
}
