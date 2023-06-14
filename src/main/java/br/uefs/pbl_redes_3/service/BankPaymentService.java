package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.exception.RequestException;
import br.uefs.pbl_redes_3.model.ClientModel;
import br.uefs.pbl_redes_3.model.JointAccountModel;
import br.uefs.pbl_redes_3.model.PaymentsModel;
import br.uefs.pbl_redes_3.model.PrivateAccountModel;
import br.uefs.pbl_redes_3.repository.*;
import br.uefs.pbl_redes_3.request.PaymentRequest;
import br.uefs.pbl_redes_3.response.PaymentResponse;
import br.uefs.pbl_redes_3.utils.Banks;
import br.uefs.pbl_redes_3.utils.PropertiesManager;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BankPaymentService {
    private final PropertiesManager propertiesManager;
    private final PrivateAccountRepository privateAccountRepository;
    private final JointAccountRepository jointAccountRepository;
    private final ModelMapper modelMapper;
    private final Banks banks;
    private final ClientRepository clientRepository;
    private final PrivatePaymentRepository privatePaymentRepository;
    private final JointPaymentRepository jointPaymentRepository;

    public BankPaymentService(final PropertiesManager propertiesManager,
                              final PrivateAccountRepository privateAccountRepository,
                              JointAccountRepository jointAccountRepository, final ModelMapper modelMapper,
                              final Banks banks, ClientRepository clientRepository, PrivatePaymentRepository privatePaymentRepository, JointPaymentRepository jointPaymentRepository) {
        this.propertiesManager = propertiesManager;
        this.privateAccountRepository = privateAccountRepository;
        this.jointAccountRepository = jointAccountRepository;
        this.modelMapper = modelMapper;
        this.banks = banks;
        this.clientRepository = clientRepository;
        this.privatePaymentRepository = privatePaymentRepository;
        this.jointPaymentRepository = jointPaymentRepository;
    }

    public PaymentResponse privateCreate(PaymentRequest request) {
        int bankId = Integer.parseInt(propertiesManager.getProperty("bank.id"));
        if (request.getSourceBankId() == bankId) {
            Optional<ClientModel> optionalClient = clientRepository.findByCpf(request.getCpf());
            if (optionalClient.isEmpty()) {
                throw new RequestException(HttpStatus.FORBIDDEN, "CLIENT");
            }
            ClientModel clientModel = optionalClient.get();

            Optional<PrivateAccountModel> sourcePrivateAccountOptional = privateAccountRepository.findByClientId(clientModel.getId());
            if (sourcePrivateAccountOptional.isPresent()) {
                PrivateAccountModel sourcePrivateAccount = sourcePrivateAccountOptional.get();
                if (sourcePrivateAccount.getBalance() >= request.getValue()) {
                    sourcePrivateAccount.setBalance(sourcePrivateAccount.getBalance() - request.getValue());
                    Optional<PrivateAccountModel> result = privateAccountRepository.update(sourcePrivateAccount);
                    PaymentsModel payment = new PaymentsModel();
                    payment.setValue(request.getValue());
                    payment.setAccountId(sourcePrivateAccount.getId());
                    payment.setDate(LocalDateTime.now());
                    return modelMapper.map(privatePaymentRepository.save(payment), PaymentResponse.class);

                } else {
                    throw new RequestException(HttpStatus.UNAUTHORIZED, "INSUFICIENT BALANCE");
                }
            } else {
                throw new RequestException(HttpStatus.NOT_FOUND, "PRIVATE ACCOUNT");
            }
        } else {
            throw new RequestException(HttpStatus.BAD_REQUEST, "INVALID BANK ID");
        }
    }

    public PaymentResponse jointCreate(PaymentRequest request) {
        int bankId = Integer.parseInt(propertiesManager.getProperty("bank.id"));
        if (request.getSourceBankId() == bankId) {
            Optional<ClientModel> optionalClient = clientRepository.findByCpf(request.getCpf());
            if (optionalClient.isEmpty()) {
                throw new RequestException(HttpStatus.FORBIDDEN, "CLIENT");
            }
            ClientModel clientModel = optionalClient.get();

            Optional<JointAccountModel> sourceJointAccountOptional = jointAccountRepository.findByClientId(clientModel.getId());
            if (sourceJointAccountOptional.isPresent()) {
                JointAccountModel sourceJointAccount = sourceJointAccountOptional.get();
                if (sourceJointAccount.getBalance() >= request.getValue()) {
                    sourceJointAccount.setBalance(sourceJointAccount.getBalance() - request.getValue());
                    Optional<JointAccountModel> result = jointAccountRepository.update(sourceJointAccount);
                    PaymentsModel payment = new PaymentsModel();
                    payment.setValue(request.getValue());
                    payment.setAccountId(sourceJointAccount.getId());
                    payment.setDate(LocalDateTime.now());
                    return modelMapper.map(jointPaymentRepository.save(payment), PaymentResponse.class);

                } else {
                    throw new RequestException(HttpStatus.UNAUTHORIZED, "INSUFICIENT BALANCE");
                }
            } else {
                throw new RequestException(HttpStatus.NOT_FOUND, "JOINT ACCOUNT");
            }
        } else {
            throw new RequestException(HttpStatus.BAD_REQUEST, "INVALID BANK ID");
        }
    }
}
