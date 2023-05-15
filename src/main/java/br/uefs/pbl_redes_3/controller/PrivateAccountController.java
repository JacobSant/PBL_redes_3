package br.uefs.pbl_redes_3.controller;

import br.uefs.pbl_redes_3.request.PrivateAccountRequest;
import br.uefs.pbl_redes_3.response.PrivateAccountResponse;
import br.uefs.pbl_redes_3.service.PrivateAccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrivateAccountController {
    private final PrivateAccountService privateAccountService;

    public PrivateAccountController(final PrivateAccountService privateAccountService) {
        this.privateAccountService = privateAccountService;
    }

    @PostMapping("/bank_account")
    public PrivateAccountResponse create(final PrivateAccountRequest request){
        return privateAccountService.create(request);
    }
}
