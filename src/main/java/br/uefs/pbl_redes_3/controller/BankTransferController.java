package br.uefs.pbl_redes_3.controller;

import br.uefs.pbl_redes_3.request.TransferRequest;
import br.uefs.pbl_redes_3.response.TransferResponse;
import br.uefs.pbl_redes_3.service.BankTransferService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pass_transfer")
public class BankTransferController {
    private final BankTransferService bankTransferService;
    public BankTransferController(final BankTransferService bankTransferService) {
        this.bankTransferService = bankTransferService;
    }

    @PostMapping("/private_account")
    public TransferResponse passPrivateTransfer(@RequestBody TransferRequest request){
        return bankTransferService.create(request);
    }

    @PostMapping("/joint_account")
    public String passJoinTransfer(@RequestBody TransferRequest request){
        return null;
    }
}
