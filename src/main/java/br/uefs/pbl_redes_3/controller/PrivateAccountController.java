package br.uefs.pbl_redes_3.controller;

import br.uefs.pbl_redes_3.request.PrivateAccountRequest;
import br.uefs.pbl_redes_3.response.ClientResponse;
import br.uefs.pbl_redes_3.response.PrivateAccountResponse;
import br.uefs.pbl_redes_3.service.PrivateAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @GetMapping()
    public PrivateAccountResponse findByClientId(@RequestParam String clientId){
        UUID id = UUID.fromString(clientId);
        return privateAccountService.findByClientID(id);
    }



}
