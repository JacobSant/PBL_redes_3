package br.uefs.pbl_redes_3.utils;

import br.uefs.pbl_redes_3.model.Bank;
import br.uefs.pbl_redes_3.model.TransactionModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.LinkedList;

@Getter
public class Synchronizer {
    private static int clockLogic = 0;
    private static LinkedList<TransactionModel> listTransactions;

    public void ajustClockLogic(TransactionModel transaction){
        clockLogic++;
        if(transaction.getRequestTime() >= clockLogic){
            clockLogic = transaction.getRequestTime() +1;
        }
        listTransactions.add(transaction);
        sortList();
    }

    private void sortList(){
        listTransactions.sort(Comparator.comparingInt(TransactionModel::getRequestTime));
    }

    public void ajustListTrasactions(){
        final RestTemplate request = new RestTemplate();
        OtherBanks.getBanksReference().forEach(t -> {
            Gson gson = new Gson();
            String message = gson.toJson(listTransactions);
            ResponseEntity<String> response = request.postForEntity(t.getIp() +"://"+t.getPort(), message, String.class);
            if(response.getStatusCode().value() == 200){

            }
        });
    }
}
