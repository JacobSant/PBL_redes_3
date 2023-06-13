package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.exception.RequestException;
import br.uefs.pbl_redes_3.model.ClientModel;
import br.uefs.pbl_redes_3.model.PrivateAccountModel;
import br.uefs.pbl_redes_3.repository.ClientRepository;
import br.uefs.pbl_redes_3.repository.PrivateAccountRepository;
import br.uefs.pbl_redes_3.request.PaymentRequest;
import br.uefs.pbl_redes_3.request.TransferRequest;
import br.uefs.pbl_redes_3.response.PaymentResponse;
import br.uefs.pbl_redes_3.utils.Banks;
import br.uefs.pbl_redes_3.utils.PropertiesManager;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankPaymentService {
    private final PropertiesManager propertiesManager;
    private final PrivateAccountRepository privateAccountRepository;
    private final ModelMapper modelMapper;
    private final Banks banks;
    private final ClientRepository clientRepository;

    public BankPaymentService(final PropertiesManager propertiesManager,
                              final PrivateAccountRepository privateAccountRepository,
                              final ModelMapper modelMapper,
                              final Banks banks, ClientRepository clientRepository) {
        this.propertiesManager = propertiesManager;
        this.privateAccountRepository = privateAccountRepository;
        this.modelMapper = modelMapper;
        this.banks = banks;
        this.clientRepository = clientRepository;
    }

    public PaymentResponse create(PaymentRequest request) {
        int bankId = Integer.parseInt(propertiesManager.getProperty("bank.id"));
        // transferencia interna
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

                    // adicionar tranfesrência no repositório
                    // Mudar retorno. model mapper receberá uma representação da tranferência e não da conta
                    Optional<PrivateAccountModel> result = privateAccountRepository.update(sourcePrivateAccount);
                    return modelMapper.map(result.get(), PaymentResponse.class);

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
}
