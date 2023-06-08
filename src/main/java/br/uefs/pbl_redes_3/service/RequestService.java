package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.model.TransactionModel;
import br.uefs.pbl_redes_3.utils.Banks;
import br.uefs.pbl_redes_3.utils.Synchronizer;
import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;

@Service
public class RequestService {
    private final Synchronizer synchronizer;

    private final Banks banks;

    public RequestService(Synchronizer synchronizer, Banks banks) {
        this.synchronizer = synchronizer;
        this.banks = banks;
    }

    private void sortList(){
        synchronizer.getListTransactions().sort(Comparator.comparingInt(TransactionModel::getRequestTime));
    }

    public void receive(TransactionModel transaction){
        synchronizer.incrementClock();
        if(transaction.getRequestTime() >= synchronizer.getClockLogic()){
            synchronizer.setClockLogic(transaction.getRequestTime() +1);
        }
        synchronizer.getListTransactions().add(transaction);
        sortList();
        returnACK(transaction);
    }

    public void returnACK(TransactionModel transaction){
        final RestTemplate request = new RestTemplate();
        banks.getBanksReference().forEach(t -> {
            Gson gson = new Gson();
            String message = gson.toJson(transaction);
            request.postForEntity(t.getIp() +"://"+t.getPort()+"/ack", message, String.class);
        });
    }
}
