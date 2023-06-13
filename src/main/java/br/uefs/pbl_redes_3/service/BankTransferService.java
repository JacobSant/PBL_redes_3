package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.exception.RequestException;
import br.uefs.pbl_redes_3.model.Bank;
import br.uefs.pbl_redes_3.model.ClientModel;
import br.uefs.pbl_redes_3.model.PrivateAccountModel;
import br.uefs.pbl_redes_3.repository.ClientRepository;
import br.uefs.pbl_redes_3.repository.PrivateAccountRepository;
import br.uefs.pbl_redes_3.request.TransferRequest;
import br.uefs.pbl_redes_3.response.TransferResponse;
import br.uefs.pbl_redes_3.utils.Banks;
import br.uefs.pbl_redes_3.utils.PropertiesManager;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class BankTransferService {

    private final PropertiesManager propertiesManager;
    private final PrivateAccountRepository privateAccountRepository;
    private final ModelMapper modelMapper;
    private final Banks banks;
    private final ClientRepository clientRepository;

    public BankTransferService(final PropertiesManager propertiesManager,
                               final PrivateAccountRepository privateAccountRepository,
                               final ModelMapper modelMapper,
                               final Banks banks, ClientRepository clientRepository) {
        this.propertiesManager = propertiesManager;
        this.privateAccountRepository = privateAccountRepository;
        this.modelMapper = modelMapper;
        this.banks = banks;
        this.clientRepository = clientRepository;
    }

    public TransferResponse create(TransferRequest request) {
        int bankId = Integer.parseInt(propertiesManager.getProperty("bank.id"));
        System.out.println(bankId);
        System.out.println(request.getSourceBankId());
        System.out.println(request.getDestinyBankId());
        System.out.println("-----------");

        // transferencia interna
        if (request.getSourceBankId() == bankId && request.getDestinyBankId() == bankId) {
            Optional<ClientModel> optionalClient = clientRepository.findByCpf(request.getCpf());
            if(optionalClient.isEmpty()){
                throw new RequestException(HttpStatus.FORBIDDEN, "CLIENT");
            }
            ClientModel clientModel = optionalClient.get();

            Optional<PrivateAccountModel> sourcePrivateAccountOptional = privateAccountRepository.findByClientId(clientModel.getId());
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
                    throw new RequestException(HttpStatus.UNAUTHORIZED, "INSUFICIENT BALANCE");
                }
            } else {
                throw new RequestException(HttpStatus.NOT_FOUND, "PRIVATE ACCOUNT");
            }

            // Banco de destino
        } else if (request.getSourceBankId() != bankId && request.getDestinyBankId() == bankId) {
            Optional<PrivateAccountModel> destinyPrivateAccountOptional = privateAccountRepository.findByAccountNumber(request.getDestinyPrivateAccountNumber());
            if (destinyPrivateAccountOptional.isPresent()) {
                PrivateAccountModel destinyPrivateAccount = destinyPrivateAccountOptional.get();
                destinyPrivateAccount.setBalance(destinyPrivateAccount.getBalance() + request.getValue());
                // Salvar transferência no repositório
                // Mudar retorno. model mapper receberá uma representação da tranferência e não da conta
                Optional<PrivateAccountModel> updatedPrivateAccountOptional = privateAccountRepository.update(destinyPrivateAccount);
                return null;
            } else {
                throw new RequestException(HttpStatus.NOT_FOUND, "PRIVATE ACCOUNT");
            }
        } else if (request.getSourceBankId() == bankId && request.getDestinyBankId() != bankId) {
            Optional<ClientModel> optionalClient = clientRepository.findByCpf(request.getCpf());
            if(optionalClient.isEmpty()){
                throw new RequestException(HttpStatus.FORBIDDEN, "CLIENT");
            }
            ClientModel clientModel = optionalClient.get();

            Optional<PrivateAccountModel> sourcePrivateAccountOptional = privateAccountRepository.findByClientId(clientModel.getId());
            if (sourcePrivateAccountOptional.isPresent()) {
                System.out.println("-----------------333");
                PrivateAccountModel sourcePrivateAccount = sourcePrivateAccountOptional.get();
                if (sourcePrivateAccount.getBalance() < request.getValue()) {
                    throw new RequestException(HttpStatus.UNAUTHORIZED, "INSUFFICIENT BALANCE");
                }

                RestTemplate httpRequest = new RestTemplate();
                if (banks.getBanksReference().stream().anyMatch(b -> b.getId() == request.getDestinyBankId())) {
                    System.out.println("-----------------3333");
                    Bank bank = banks.getBanksReference().stream().filter(b -> b.getId() == request.getDestinyBankId()).findFirst().get();
                    String url = "http://" + bank.getIp() + ":" + bank.getPort() + "/pass_transfer/private_account";
                    try {
                        ResponseEntity<TransferResponse> response = httpRequest.postForEntity(url, request, TransferResponse.class);
                        System.out.println("Linha 98 BankTransfer");
                        if (response.getStatusCodeValue() == 200) {
                            sourcePrivateAccount.setBalance(sourcePrivateAccount.getBalance() - request.getValue());
                            // Salvar tranferência no repositório
                            // Mudar retorno. model mapper receberá uma representação da tranferência e não da conta
                            System.out.println("Linha 93 BankTransfer");

                            return modelMapper.map(sourcePrivateAccount, TransferResponse.class);
                        } else {
                            HttpStatus responseStatus = response.getStatusCode();
                            throw new RequestException(responseStatus);
                        }
                    }catch (HttpClientErrorException e){
                        throw new RequestException(HttpStatus.NOT_FOUND,"PRIVATE ACCOUNT");
                    }

                } else {
                    throw new RequestException(HttpStatus.BAD_REQUEST, "INVALID DESTINY BANK ID");
                }
            } else {
                throw new RequestException(HttpStatus.BAD_REQUEST, "INVALID SOURCE BANK ID");
            }
        }
        throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL ERROR");
    }
}
