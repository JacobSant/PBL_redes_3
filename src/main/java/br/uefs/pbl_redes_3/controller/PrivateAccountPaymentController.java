package br.uefs.pbl_redes_3.controller;

import br.uefs.pbl_redes_3.exception.RequestException;
import br.uefs.pbl_redes_3.request.PaymentRequest;
import br.uefs.pbl_redes_3.response.PaymentResponse;
import br.uefs.pbl_redes_3.service.PrivateAccountPaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController("/private/payment")
public class PrivateAccountPaymentController {
    private final PrivateAccountPaymentService privateAccountPaymentService;

    public PrivateAccountPaymentController(final PrivateAccountPaymentService privateAccountPaymentService) {
        this.privateAccountPaymentService = privateAccountPaymentService;
    }

    @PostMapping()
    public PaymentResponse create(@RequestBody PaymentRequest request, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("token");
        if(token == null){
            throw new RequestException(HttpStatus.FORBIDDEN,"ACCESS TOKEN NOT FOUND");
        }
        if (token.isEmpty() || token.isBlank()){
            throw new RequestException(HttpStatus.FORBIDDEN,"ACCESS TOKEN NOT FOUND");
        }

        return privateAccountPaymentService.create(request,token);
    }

}
