package br.uefs.pbl_redes_3.controller;

import br.uefs.pbl_redes_3.request.PaymentRequest;
import br.uefs.pbl_redes_3.response.PaymentResponse;
import br.uefs.pbl_redes_3.service.BankPaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pass_payment")
public class BankPaymentController {
    private final BankPaymentService bankPaymentService;

    public BankPaymentController(BankPaymentService bankPaymentService) {
        this.bankPaymentService = bankPaymentService;
    }

    @PostMapping("/private_account")
    public PaymentResponse bankPayment(@RequestBody PaymentRequest paymentRequest){
        System.out.println(paymentRequest.getCpf());
        return bankPaymentService.create(paymentRequest);
    }

}
