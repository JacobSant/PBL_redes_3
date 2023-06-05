package br.uefs.pbl_redes_3.utils;

import br.uefs.pbl_redes_3.exception.RequestException;
import br.uefs.pbl_redes_3.model.Bank;
import br.uefs.pbl_redes_3.model.OperationModel;
import br.uefs.pbl_redes_3.model.TransactionModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.UUID;
@Component
public class Synchronizer {
    private static int clockLogic = 0;
    private static LinkedList<TransactionModel> listTransactions;

    public int getClockLogic(){
        return clockLogic;
    }

    public LinkedList<TransactionModel> getListTransactions() {
        return listTransactions;
    }


    public void send(OperationModel operation){
        UUID id = UUID.nameUUIDFromBytes((operation.getSourceBank().getBankId().toString()+
                operation.getDate().toString()+
                operation.getSourceClient().getId().toString()).getBytes());
        TransactionModel transaction = new TransactionModel(id, ++clockLogic,  0);

        final RestTemplate request = new RestTemplate();
        OtherBanks.getBanksReference().forEach(t -> {
            Gson gson = new Gson();
            String message = gson.toJson(transaction);
            ResponseEntity<String> response = request.postForEntity(t.getIp() +"://"+t.getPort()+"/request", message, String.class);
            if(response.getStatusCode().value() != 200){
                throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR, "Algum servidor não recebeu");
            }
        }
        );

        while(listTransactions.contains(transaction)){}

    }

    public void setClockLogic(int clockLogic) {
        Synchronizer.clockLogic = clockLogic;
    }

    public void setListTransactions(LinkedList<TransactionModel> listTransactions) {
        Synchronizer.listTransactions = listTransactions;
    }


    public void incrementClock(){
        clockLogic++;
    }
}
