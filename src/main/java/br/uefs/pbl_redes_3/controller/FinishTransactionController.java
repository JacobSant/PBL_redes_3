package br.uefs.pbl_redes_3.controller;

import br.uefs.pbl_redes_3.service.FinishTransactionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/finish")
public class FinishTransactionController {
    private final FinishTransactionService finishTransactionService;

    public FinishTransactionController(FinishTransactionService finishTransactionService) {
        this.finishTransactionService = finishTransactionService;
    }

    @PostMapping()
    public Boolean finish(){
        return finishTransactionService.finish();

    }
}
