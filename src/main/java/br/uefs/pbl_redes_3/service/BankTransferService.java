package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.exception.RequestException;
import br.uefs.pbl_redes_3.model.Bank;
import br.uefs.pbl_redes_3.model.PrivateAccountModel;
import br.uefs.pbl_redes_3.repository.PrivateAccountRepository;
import br.uefs.pbl_redes_3.request.TransferRequest;
import br.uefs.pbl_redes_3.response.TransferResponse;
import br.uefs.pbl_redes_3.utils.Banks;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class BankTransferService {
    private final PrivateAccountRepository privateAccountRepository;

    private final ModelMapper modelMapper;
    @Value("${bank.id}")
    private int bankId;
    private final Banks banks;

    public BankTransferService(final PrivateAccountRepository privateAccountRepository,
                               final ModelMapper modelMapper,
                               final Banks banks) {
        this.privateAccountRepository = privateAccountRepository;
        this.modelMapper = modelMapper;
        this.banks = banks;
    }

    public TransferResponse create(TransferRequest request) {
        if (request.getSourceBankId() == bankId && request.getDestinyBankId() == bankId) {
            Optional<PrivateAccountModel> sourcePrivateAccountOptional = privateAccountRepository.findByAccountNumber(request.getSourcePrivateAccountNumber());
            Optional<PrivateAccountModel> destinyPrivateAccountOptional = privateAccountRepository.findByAccountNumber(request.getDestinyPrivateAccountNumber());
            if (sourcePrivateAccountOptional.isPresent() && destinyPrivateAccountOptional.isPresent()) {
                PrivateAccountModel sourcePrivateAccount = sourcePrivateAccountOptional.get();
                PrivateAccountModel destinyPrivateAccount = destinyPrivateAccountOptional.get();
                if (sourcePrivateAccount.getBalance() >= request.getValue()) {
                    sourcePrivateAccount.setBalance(sourcePrivateAccount.getBalance() - request.getValue());
                    destinyPrivateAccount.setBalance(destinyPrivateAccount.getBalance() + request.getValue());

                    // adicionar tranfesrência no repositório
                    // Mudar retorno. model mapper receberá uma representação da tranferência e não da conta
                    privateAccountRepository.update(destinyPrivateAccount);
                    return modelMapper.map(privateAccountRepository.update(sourcePrivateAccount), TransferResponse.class);
                } else {
                    throw new RequestException(HttpStatus.UNAUTHORIZED, "INSUFFICIENT BALANCE");
                }
            } else {
                throw new RequestException(HttpStatus.NOT_FOUND, "PRIVATE ACCOUNT");
            }
        } else if (request.getSourceBankId() != bankId && request.getDestinyBankId() == bankId) {
            Optional<PrivateAccountModel> destinyPrivateAccountOptional = privateAccountRepository.findByAccountNumber(request.getDestinyPrivateAccountNumber());
            if (destinyPrivateAccountOptional.isPresent()) {
                PrivateAccountModel destinyPrivateAccount = destinyPrivateAccountOptional.get();
                destinyPrivateAccount.setBalance(destinyPrivateAccount.getBalance() + request.getValue());

                // Salvar transferência no repositório
                // Mudar retorno. model mapper receberá uma representação da tranferência e não da conta
                return modelMapper.map(privateAccountRepository.update(destinyPrivateAccount), TransferResponse.class);
            } else {
                throw new RequestException(HttpStatus.NOT_FOUND, "PRIVATE ACCOUNT");
            }
        } else if (request.getSourceBankId() == bankId && request.getDestinyBankId() != bankId) {
            Optional<PrivateAccountModel> sourcePrivateAccountOptional = privateAccountRepository.findByAccountNumber(request.getSourcePrivateAccountNumber());
            if (sourcePrivateAccountOptional.isPresent()) {
                PrivateAccountModel sourcePrivateAccount = sourcePrivateAccountOptional.get();
                sourcePrivateAccount.setBalance(sourcePrivateAccount.getBalance() - request.getValue());
                RestTemplate httpRequest = new RestTemplate();
                if (banks.getBanksReference().stream().anyMatch(b -> b.getId() == request.getDestinyBankId())) {
                    Bank bank = banks.getBanksReference().stream().filter(b -> b.getId() == request.getDestinyBankId()).findFirst().get();
                    ResponseEntity<String> response = httpRequest.postForEntity(bank.getIp() + "://" + bank.getPort() + "/pass_transfer/private_account", request, String.class);
                    if (response.getStatusCodeValue() == 200) {
                       // Salvar tranferência no repositório
                        // Mudar retorno. model mapper receberá uma representação da tranferência e não da conta
                        return modelMapper.map(response.getBody(), TransferResponse.class);
                    } else {
                        HttpStatus responseStatus = response.getStatusCode();
                        throw new RequestException(responseStatus, response.getBody());
                    }
                } else {
                    throw new RequestException(HttpStatus.BAD_REQUEST, "INVALID BANK ID");
                }
            }
        }

        throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR,"INTERNAL ERROR");
    }
}