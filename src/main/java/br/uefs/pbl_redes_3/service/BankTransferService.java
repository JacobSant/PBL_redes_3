package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.exception.RequestException;
import br.uefs.pbl_redes_3.model.Bank;
import br.uefs.pbl_redes_3.model.PrivateAccountModel;
import br.uefs.pbl_redes_3.repository.PrivateAccountRepository;
import br.uefs.pbl_redes_3.request.TransferRequest;
import br.uefs.pbl_redes_3.response.TransferResponse;
import br.uefs.pbl_redes_3.utils.Banks;
import br.uefs.pbl_redes_3.utils.PropertiesManager;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Optional;

@Service
public class BankTransferService {

    private final PropertiesManager propertiesManager;
    private final PrivateAccountRepository privateAccountRepository;
    private final ModelMapper modelMapper;
    private final Banks banks;

    public BankTransferService(final PropertiesManager propertiesManager,
                               final PrivateAccountRepository privateAccountRepository,
                               final ModelMapper modelMapper,
                               final Banks banks) {
        this.propertiesManager = propertiesManager;
        this.privateAccountRepository = privateAccountRepository;
        this.modelMapper = modelMapper;
        this.banks = banks;
    }

    public TransferResponse create(TransferRequest request) {
        int bankId = Integer.parseInt(propertiesManager.getProperty("bank.id"));
        System.out.println(bankId);
        System.out.println(request.getSourceBankId());
        System.out.println(request.getDestinyBankId());
        System.out.println("-----------");
        if (request.getSourceBankId() == bankId && request.getDestinyBankId() == bankId) {
            System.out.println("---------1");
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
                    Optional<PrivateAccountModel> result = privateAccountRepository.update(sourcePrivateAccount);
                    privateAccountRepository.update(destinyPrivateAccount);
                    System.out.println("Linha 57 BankTransfer");
                    return modelMapper.map(result.get(), TransferResponse.class);

                } else {
                    throw new RequestException(HttpStatus.UNAUTHORIZED,"INSUFICIENT BALANCE");
                }
            } else {
                throw new RequestException(HttpStatus.NOT_FOUND, "PRIVATE ACCOUNT");
            }
        }

        else if (request.getSourceBankId() != bankId && request.getDestinyBankId() == bankId) {
            System.out.println("--------------- 22");
            Optional<PrivateAccountModel> destinyPrivateAccountOptional = privateAccountRepository.findByAccountNumber(request.getDestinyPrivateAccountNumber());
            if (destinyPrivateAccountOptional.isPresent()) {
                PrivateAccountModel destinyPrivateAccount = destinyPrivateAccountOptional.get();
                System.out.println(destinyPrivateAccount.getBalance());
                destinyPrivateAccount.setBalance(destinyPrivateAccount.getBalance() + request.getValue());
                System.out.println(destinyPrivateAccount.getBalance());
                // Salvar transferência no repositório
                // Mudar retorno. model mapper receberá uma representação da tranferência e não da conta
                System.out.println("Linha 74 BankTransfer");

                return modelMapper.map(privateAccountRepository.update(destinyPrivateAccount), TransferResponse.class);
            } else {
                throw new RequestException(HttpStatus.NOT_FOUND, "PRIVATE ACCOUNT");
            }
        } else if (request.getSourceBankId() == bankId && request.getDestinyBankId() != bankId) {
            System.out.println("-----------------33");
            Optional<PrivateAccountModel> sourcePrivateAccountOptional = privateAccountRepository.findByAccountNumber(request.getSourcePrivateAccountNumber());
            if (sourcePrivateAccountOptional.isPresent()) {
                System.out.println("-----------------333");
                PrivateAccountModel sourcePrivateAccount = sourcePrivateAccountOptional.get();
                sourcePrivateAccount.setBalance(sourcePrivateAccount.getBalance() - request.getValue());
                RestTemplate httpRequest = new RestTemplate();
                if (banks.getBanksReference().stream().anyMatch(b -> b.getId() == request.getDestinyBankId())) {
                    System.out.println("-----------------3333");
                    Bank bank = banks.getBanksReference().stream().filter(b -> b.getId() == request.getDestinyBankId()).findFirst().get();
                    String url = "http://" + bank.getIp() + ":" + bank.getPort() + "/pass_transfer/private_account";
                    ResponseEntity<String> response = httpRequest.postForEntity(url, request, String.class);
                    System.out.println("Linha 98 BankTransfer");
                    if (response.getStatusCodeValue() == 200) {
                        // Salvar tranferência no repositório
                        // Mudar retorno. model mapper receberá uma representação da tranferência e não da conta
                        System.out.println("Linha 93 BankTransfer");

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

        throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL ERROR");
    }
}
