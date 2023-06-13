package br.uefs.pbl_redes_3.controller;

import br.uefs.pbl_redes_3.model.TransactionModel;
import br.uefs.pbl_redes_3.service.ReceiveAckService;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ack")
public class ReceiveAckController {

    private final ReceiveAckService receiveAckService;

    public ReceiveAckController(ReceiveAckService receiveAckService) {
        this.receiveAckService = receiveAckService;
    }

    @PostMapping()
    public String receive(@RequestBody String request){
        System.out.println( "Receive Ack Service");
        Gson gson = new Gson();
        TransactionModel transactionModel = gson.fromJson(request, TransactionModel.class);
        receiveAckService.receivedACK(transactionModel);
        return "";
    }
}
