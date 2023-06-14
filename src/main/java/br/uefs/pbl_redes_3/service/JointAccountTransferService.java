package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.exception.RequestException;
import br.uefs.pbl_redes_3.model.Bank;
import br.uefs.pbl_redes_3.model.ClientModel;
import br.uefs.pbl_redes_3.model.TokenModel;
import br.uefs.pbl_redes_3.repository.ClientRepository;
import br.uefs.pbl_redes_3.repository.TokenRepository;
import br.uefs.pbl_redes_3.request.TransferRequest;
import br.uefs.pbl_redes_3.response.TransferResponse;
import br.uefs.pbl_redes_3.utils.Banks;
import br.uefs.pbl_redes_3.utils.Synchronizer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Optional;

@Service
public class JointAccountTransferService {
    private final TokenRepository tokenRepository;
    private final Banks banks;
    private final Synchronizer synchronizer;
    private final ClientRepository clientRepository;

    public JointAccountTransferService(TokenRepository tokenRepository, Banks banks, Synchronizer synchronizer, ClientRepository clientRepository) {
        this.tokenRepository = tokenRepository;
        this.banks = banks;
        this.synchronizer = synchronizer;
        this.clientRepository = clientRepository;
    }

    public TransferResponse create(TransferRequest request, String token) {
        Optional<TokenModel> tokenOptional = tokenRepository.findByToken(token);

        if (tokenOptional.isPresent()) {

            TokenModel tokenModel = tokenOptional.get();
            Date currentDate = new Date();
            if (currentDate.getTime() < tokenModel.getExpiresAt()) {
                Optional<ClientModel> optionalClient = clientRepository.findById(tokenModel.getClientId());
                ClientModel clientModel = optionalClient.get();
                request.setCpf(clientModel.getCpf());
                synchronizer.send(request.getCpf(), request.getSourceBankId());
                RestTemplate httpRequest = new RestTemplate();
                if (banks.getBanksReference().stream().anyMatch(b -> b.getId() == request.getSourceBankId())) {
                    Bank bank = banks.getBanksReference().stream()
                            .filter(b -> b.getId() == request.getSourceBankId()).findFirst().get();
                    String url = "http://" + bank.getIp() + ":" + bank.getPort() + "/pass_transfer/"+request.getDestinyAccountType();
                    try {
                        ResponseEntity<TransferResponse> response = httpRequest.postForEntity(url, request, TransferResponse.class);
                        TransferResponse result = response.getBody();
                        finish();
                        return result;
                    } catch (HttpClientErrorException e) {

                        finish();
                        switch (e.getStatusCode()) {
                            case FORBIDDEN -> throw new RequestException(HttpStatus.FORBIDDEN, "CLIENT");

                            case NOT_FOUND -> throw new RequestException(HttpStatus.NOT_FOUND, "JOINT ACCOUNT");
                            case UNAUTHORIZED ->
                                    throw new RequestException(HttpStatus.UNAUTHORIZED, "INSUFFICIENT BALANCE");
                            case BAD_REQUEST -> throw new RequestException(HttpStatus.BAD_REQUEST, "INVALID BANK ID");
                            case INTERNAL_SERVER_ERROR ->
                                    throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL ERROR");
                        }
                        throw new RequestException(e.getStatusCode());
                    }
                } else {
                    finish();
                    throw new RequestException(HttpStatus.BAD_REQUEST, "INVALID BANK ID");
                }
            } else {
                finish();
                throw new RequestException(HttpStatus.UNAUTHORIZED, "ACCESS TOKEN EXPIRED");
            }
        } else {
            finish();
            throw new RequestException(HttpStatus.FORBIDDEN, "INVALID TOKEN");
        }
    }

    private void finish() {
        System.out.println("JOINT Account Tranfer Service linha 65");
        final RestTemplate request = new RestTemplate();
        banks.getBanksReference().forEach(t -> {
                    String url = "http://" + t.getIp() + ":" + t.getPort() + "/finish";
                    System.out.println(url);
                    ResponseEntity<Boolean> response = request.postForEntity(url, "", Boolean.class);
                    if (response.getStatusCode().value() != 200) {
                        throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR, "Algum servidor não recebeu");
                    }
                }
        );

    }
}