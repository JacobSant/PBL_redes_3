package br.uefs.pbl_redes_3.controller;

import br.uefs.pbl_redes_3.request.PrivateAccountRequest;
import br.uefs.pbl_redes_3.response.PrivateAccountResponse;
import br.uefs.pbl_redes_3.service.PrivateAccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank_account_private")
public class PrivateAccountController {
    private final PrivateAccountService privateAccountService;

    public PrivateAccountController(final PrivateAccountService privateAccountService) {
        this.privateAccountService = privateAccountService;
    }

    @PostMapping()
    public PrivateAccountResponse create(@RequestBody final PrivateAccountRequest privateAccountRequest){
        return privateAccountService.create(privateAccountRequest);
    }

}
