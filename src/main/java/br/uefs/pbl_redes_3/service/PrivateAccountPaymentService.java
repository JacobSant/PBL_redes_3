package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.repository.PrivateAccountRepository;
import br.uefs.pbl_redes_3.repository.TokenRepository;
import br.uefs.pbl_redes_3.request.PaymentRequest;
import br.uefs.pbl_redes_3.response.PaymentResponse;
import br.uefs.pbl_redes_3.utils.Banks;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class PrivateAccountPaymentService {
    private final TokenRepository tokenRepository;
    private final ModelMapper modelMapper;
    private final Banks banks;
    private final PrivateAccountRepository privateAccountRepository;

    public PrivateAccountPaymentService(TokenRepository tokenRepository,
                                        ModelMapper modelMapper,
                                        Banks banks,
                                        PrivateAccountRepository privateAccountRepository) {
        this.tokenRepository = tokenRepository;
        this.modelMapper = modelMapper;
        this.banks = banks;
        this.privateAccountRepository = privateAccountRepository;
    }

    public PaymentResponse create(PaymentRequest request, String token) {
        return null;
    }
}
