package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.exception.RequestException;
import br.uefs.pbl_redes_3.model.*;
import br.uefs.pbl_redes_3.repository.*;
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

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BankTransferService {

    private final PropertiesManager propertiesManager;
    private final ModelMapper modelMapper;
    private final Banks banks;
    private final ClientRepository clientRepository;

    private final PrivateAccountRepository privateAccountRepository;
    private final PrivateTransferRepository transferRepository;
    private final JointAccountRepository jointAccountRepository;
    private final JointTransferRepository jointTransferRepository;


    public BankTransferService(final PropertiesManager propertiesManager,
                               final PrivateAccountRepository privateAccountRepository,
                               final ModelMapper modelMapper,
                               final Banks banks, ClientRepository clientRepository, PrivateTransferRepository transferRepository, JointAccountRepository jointAccountRepository, JointTransferRepository jointTransferRepository) {
        this.propertiesManager = propertiesManager;
        this.privateAccountRepository = privateAccountRepository;
        this.modelMapper = modelMapper;
        this.banks = banks;
        this.clientRepository = clientRepository;
        this.transferRepository = transferRepository;
        this.jointAccountRepository = jointAccountRepository;
        this.jointTransferRepository = jointTransferRepository;
    }


    public TransferResponse privateCreate(TransferRequest request) {
        System.out.println("-------" + request.getDestinyAccountNumber());
        int bankId = Integer.parseInt(propertiesManager.getProperty("bank.id"));
        // transferencia interna

        if (request.getSourceBankId() == bankId && request.getDestinyBankId() == bankId) {
            System.out.println("Transferencia interna");
            Optional<ClientModel> optionalClient = clientRepository.findByCpf(request.getCpf());
            if (optionalClient.isEmpty()) {
                throw new RequestException(HttpStatus.FORBIDDEN, "CLIENT");
            }
            ClientModel clientModel = optionalClient.get();

            Optional<PrivateAccountModel> sourcePrivateAccountOptional = privateAccountRepository.findByClientId(clientModel.getId());
            Optional<PrivateAccountModel> destinyPrivateAccountOptional = privateAccountRepository.findByAccountNumber(request.getDestinyAccountNumber());
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
                    TransfersModel transfer = new TransfersModel();
                    transfer.setValue(request.getValue());
                    transfer.setDestinyBankId(request.getDestinyBankId());
                    transfer.setSourceBankId(request.getSourceBankId());
                    transfer.setSourceAccountNumber(sourcePrivateAccount.getAccountNumber());
                    transfer.setDate(LocalDateTime.now());

                    System.out.println("Linha 57 BankTransfer");
                    return modelMapper.map(transferRepository.save(transfer), TransferResponse.class);

                } else {
                    throw new RequestException(HttpStatus.UNAUTHORIZED, "INSUFICIENT BALANCE");
                }
            } else {
                throw new RequestException(HttpStatus.NOT_FOUND, "PRIVATE ACCOUNT");
            }

            // Banco de destino
        } else if (request.getSourceBankId() != bankId && request.getDestinyBankId() == bankId) {
            System.out.println("Banco de destino");
            Optional<PrivateAccountModel> destinyPrivateAccountOptional = privateAccountRepository.findByAccountNumber(request.getDestinyAccountNumber());
            if (destinyPrivateAccountOptional.isPresent()) {
                PrivateAccountModel destinyPrivateAccount = destinyPrivateAccountOptional.get();
                destinyPrivateAccount.setBalance(destinyPrivateAccount.getBalance() + request.getValue());
                // Salvar transferência no repositório
                // Mudar retorno. model mapper receberá uma representação da tranferência e não da conta
                Optional<PrivateAccountModel> updatedPrivateAccountOptional = privateAccountRepository.update(destinyPrivateAccount);
                TransfersModel transfer = new TransfersModel();
                transfer.setValue(request.getValue());
                transfer.setDestinyBankId(request.getDestinyBankId());
                transfer.setSourceBankId(request.getSourceBankId());
                transfer.setDestinyAccountNumber(request.getDestinyAccountNumber());
                transfer.setDate(LocalDateTime.now());
                return modelMapper.map(transferRepository.save(transfer), TransferResponse.class);
            } else {
                throw new RequestException(HttpStatus.NOT_FOUND, "PRIVATE ACCOUNT");
            }
        } else if (request.getSourceBankId() == bankId && request.getDestinyBankId() != bankId) {
            System.out.println("Banco de origem");
            Optional<ClientModel> optionalClient = clientRepository.findByCpf(request.getCpf());
            if (optionalClient.isEmpty()) {
                throw new RequestException(HttpStatus.FORBIDDEN, "CLIENT");
            }
            ClientModel clientModel = optionalClient.get();

            Optional<PrivateAccountModel> sourcePrivateAccountOptional = privateAccountRepository.findByClientId(clientModel.getId());
            if (sourcePrivateAccountOptional.isPresent()) {
                System.out.println("-----------------333");

                PrivateAccountModel sourcePrivateAccount = sourcePrivateAccountOptional.get();

                System.out.println(sourcePrivateAccount.getBalance());
                System.out.println(sourcePrivateAccount.getAccountNumber());

                if (sourcePrivateAccount.getBalance() < request.getValue()) {
                    throw new RequestException(HttpStatus.UNAUTHORIZED, "INSUFFICIENT BALANCE");
                }

                RestTemplate httpRequest = new RestTemplate();

                if (banks.getBanksReference().stream().anyMatch(b -> b.getId() == request.getDestinyBankId())) {
                    System.out.println("-----------------3333");

                    Bank bank = banks.getBanksReference().stream().filter(b -> b.getId() == request.getDestinyBankId()).findFirst().get();
                    String url = "http://" + bank.getIp() + ":" + bank.getPort() + "/pass_transfer/"+request.getDestinyAccountType();
                    try {
                        ResponseEntity<TransferResponse> response = httpRequest.postForEntity(url, request, TransferResponse.class);
                        System.out.println("Linha 98 BankTransfer");
                        if (response.getStatusCodeValue() == 200) {
                            sourcePrivateAccount.setBalance(sourcePrivateAccount.getBalance() - request.getValue());
                            // Salvar tranferência no repositório
                            // Mudar retorno. model mapper receberá uma representação da tranferência e não da conta
                            System.out.println("Linha 93 BankTransfer");
                            TransfersModel transfer = new TransfersModel();
                            transfer.setValue(request.getValue());
                            transfer.setDestinyBankId(request.getDestinyBankId());
                            transfer.setSourceBankId(request.getSourceBankId());
                            transfer.setSourceAccountNumber(sourcePrivateAccount.getAccountNumber());
                            transfer.setDate(LocalDateTime.now());
                            privateAccountRepository.update(sourcePrivateAccount);
                            return modelMapper.map(transferRepository.save(transfer), TransferResponse.class);
                        } else {
                            HttpStatus responseStatus = response.getStatusCode();
                            throw new RequestException(responseStatus);
                        }
                    } catch (HttpClientErrorException e) {
                        throw new RequestException(HttpStatus.NOT_FOUND, "PRIVATE ACCOUNT");
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


    public TransferResponse jointCreate(TransferRequest request) {
        System.out.println("-------" + request.getDestinyAccountNumber());
        System.out.println();
        int bankId = Integer.parseInt(propertiesManager.getProperty("bank.id"));

        // transferencia interna
        if (request.getSourceBankId() == bankId && request.getDestinyBankId() == bankId) {
            Optional<ClientModel> optionalClient = clientRepository.findByCpf(request.getCpf());
            if (optionalClient.isEmpty()) {
                throw new RequestException(HttpStatus.FORBIDDEN, "CLIENT");
            }
            ClientModel clientModel = optionalClient.get();

            Optional<JointAccountModel> sourceJointAccountOptional = jointAccountRepository.findByClientId(clientModel.getId());
            Optional<JointAccountModel> destinyJointAccountOptional = jointAccountRepository.findByAccountNumber(request.getDestinyAccountNumber());
            if (sourceJointAccountOptional.isPresent() && destinyJointAccountOptional.isPresent()) {
                JointAccountModel sourceJointAccount = sourceJointAccountOptional.get();
                JointAccountModel destinyJointAccount = destinyJointAccountOptional.get();
                if (sourceJointAccount.getBalance() >= request.getValue()) {
                    sourceJointAccount.setBalance(sourceJointAccount.getBalance() - request.getValue());
                    destinyJointAccount.setBalance(destinyJointAccount.getBalance() + request.getValue());

                    // adicionar tranfesrência no repositório
                    // Mudar retorno. model mapper receberá uma representação da tranferência e não da conta
                    Optional<JointAccountModel> result = jointAccountRepository.update(sourceJointAccount);
                    jointAccountRepository.update(destinyJointAccount);
                    TransfersModel transfer = new TransfersModel();
                    transfer.setValue(request.getValue());
                    transfer.setDestinyBankId(request.getDestinyBankId());
                    transfer.setSourceBankId(request.getSourceBankId());
                    transfer.setSourceAccountNumber(sourceJointAccount.getAccountNumber());
                    transfer.setDate(LocalDateTime.now());

                    System.out.println("Linha 57 BankTransfer");
                    return modelMapper.map(transferRepository.save(transfer), TransferResponse.class);

                } else {
                    throw new RequestException(HttpStatus.UNAUTHORIZED, "INSUFICIENT BALANCE");
                }
            } else {
                throw new RequestException(HttpStatus.NOT_FOUND, "JOINT ACCOUNT 217");
            }

            // Banco de destino
        } else if (request.getSourceBankId() != bankId && request.getDestinyBankId() == bankId) {
            Optional<JointAccountModel> destinyJointAccountOptional = jointAccountRepository.findByAccountNumber(request.getDestinyAccountNumber());
            if (destinyJointAccountOptional.isPresent()) {
                JointAccountModel destinyJointAccount = destinyJointAccountOptional.get();
                destinyJointAccount.setBalance(destinyJointAccount.getBalance() + request.getValue());
                Optional<JointAccountModel> updatedJointAccountOptional = jointAccountRepository.update(destinyJointAccount);
                TransfersModel transfer = new TransfersModel();
                transfer.setValue(request.getValue());
                transfer.setDestinyBankId(request.getDestinyBankId());
                transfer.setSourceBankId(request.getSourceBankId());
                transfer.setDestinyAccountNumber(request.getDestinyAccountNumber());
                transfer.setDate(LocalDateTime.now());
                return modelMapper.map(transferRepository.save(transfer), TransferResponse.class);
            } else {
                throw new RequestException(HttpStatus.NOT_FOUND, "JOINT ACCOUNT 237");
            }


        } else if (request.getSourceBankId() == bankId && request.getDestinyBankId() != bankId) {
            Optional<ClientModel> optionalClient = clientRepository.findByCpf(request.getCpf());
            if (optionalClient.isEmpty()) {
                throw new RequestException(HttpStatus.FORBIDDEN, "CLIENT");
            }
            ClientModel clientModel = optionalClient.get();

            Optional<JointAccountModel> sourceJointAccountOptional = jointAccountRepository.findByClientId(clientModel.getId());
            if (sourceJointAccountOptional.isPresent()) {
                System.out.println("-----------------333");
                JointAccountModel sourceJointAccount = sourceJointAccountOptional.get();
                if (sourceJointAccount.getBalance() < request.getValue()) {
                    throw new RequestException(HttpStatus.UNAUTHORIZED, "INSUFFICIENT BALANCE");
                }

                RestTemplate httpRequest = new RestTemplate();
                if (banks.getBanksReference().stream().anyMatch(b -> b.getId() == request.getDestinyBankId())) {
                    System.out.println("-----------------3333");
                    Bank bank = banks.getBanksReference().stream().filter(b -> b.getId() == request.getDestinyBankId()).findFirst().get();
                    String url = "http://" + bank.getIp() + ":" + bank.getPort() + "/pass_transfer/"+request.getDestinyAccountType();
                    try {
                        ResponseEntity<TransferResponse> response = httpRequest.postForEntity(url, request, TransferResponse.class);
                        System.out.println("Linha 98 BankTransfer");
                        if (response.getStatusCodeValue() == 200) {
                            sourceJointAccount.setBalance(sourceJointAccount.getBalance() - request.getValue());
                            // Salvar tranferência no repositório
                            // Mudar retorno. model mapper receberá uma representação da tranferência e não da conta
                            System.out.println("Linha 93 BankTransfer");
                            TransfersModel transfer = new TransfersModel();
                            transfer.setValue(request.getValue());
                            transfer.setDestinyBankId(request.getDestinyBankId());
                            transfer.setSourceBankId(request.getSourceBankId());
                            transfer.setSourceAccountNumber(sourceJointAccount.getAccountNumber());
                            transfer.setDate(LocalDateTime.now());
                            jointAccountRepository.update(sourceJointAccount);
                            return modelMapper.map(transferRepository.save(transfer), TransferResponse.class);
                        } else {
                            HttpStatus responseStatus = response.getStatusCode();
                            throw new RequestException(responseStatus);
                        }
                    } catch (HttpClientErrorException e) {
                        throw new RequestException(HttpStatus.NOT_FOUND, "JOINT ACCOUNT 280");
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
