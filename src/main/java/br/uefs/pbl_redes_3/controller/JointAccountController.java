package br.uefs.pbl_redes_3.controller;

import br.uefs.pbl_redes_3.request.JointAccountRequest;
import br.uefs.pbl_redes_3.response.JointAccountResponse;
import br.uefs.pbl_redes_3.service.JointAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/bank_account_joint")
public class JointAccountController {
    private final JointAccountService jointAccountService;

    public JointAccountController(JointAccountService jointAccountService) {
        this.jointAccountService = jointAccountService;
    }

    @PostMapping()
    public JointAccountResponse create(@RequestBody JointAccountRequest jointAccountRequest) {
        return jointAccountService.create(jointAccountRequest);
    }

    @GetMapping()
    public JointAccountResponse findByClientId(@RequestParam String clientId){
        UUID id = UUID.fromString(clientId);
        return jointAccountService.findByClientID(id);
    }
}
