package br.uefs.pbl_redes_3.controller;

import br.uefs.pbl_redes_3.request.JointAccountRequest;
import br.uefs.pbl_redes_3.response.JointAccountResponse;
import br.uefs.pbl_redes_3.service.JointAccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank_account_joint")
public class JointAccountController {
    private final JointAccountService jointAccountService;

    public JointAccountController(JointAccountService jointAccountService) {
        this.jointAccountService = jointAccountService;
    }

    @PostMapping()
    public JointAccountResponse create(JointAccountRequest jointAccountRequest){
        return jointAccountService.create(jointAccountRequest);
    }
}
