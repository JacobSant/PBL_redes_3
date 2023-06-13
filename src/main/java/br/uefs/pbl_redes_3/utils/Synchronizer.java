package br.uefs.pbl_redes_3.utils;

import br.uefs.pbl_redes_3.exception.RequestException;
import br.uefs.pbl_redes_3.model.TransactionModel;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.UUID;

@Component
public class Synchronizer {
    private static int clockLogic = 0;
    private static LinkedList<TransactionModel> listTransactions = new LinkedList<>();
    private final Banks banks;

    public Synchronizer(Banks banks) {
        this.banks = banks;
    }

    public void incrementClock() {
        ++clockLogic;
    }

    public void send(int sourceAccount, int sourceBank) {
        incrementClock();
        UUID id = UUID.nameUUIDFromBytes(Integer.toString(sourceBank + clockLogic +
                sourceAccount).getBytes());
        TransactionModel transaction = new TransactionModel(id, clockLogic, 0, false);

        final RestTemplate request = new RestTemplate();
        try {
            banks.getBanksReference().forEach(t -> {
                        Gson gson = new Gson();
                        String message = gson.toJson(transaction);
                        String url = "http://" + t.getIp() + ":" + t.getPort() + "/request";

                        System.out.println(url);
                        ResponseEntity<TransactionModel> response = request.postForEntity(url, transaction, TransactionModel.class);
                        if (response.getStatusCode().value() != 200) {
                            throw new RequestException(HttpStatus.INTERNAL_SERVER_ERROR, "Algum servidor não recebeu");
                        }
                    }
            );
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }

        while (!listTransactions.getFirst().equals(transaction) || !listTransactions.getFirst().isExecutable()) ;
    }

    public void setClockLogic(int clockLogic) {
        Synchronizer.clockLogic = clockLogic;
    }

    public void setListTransactions(LinkedList<TransactionModel> listTransactions) {
        Synchronizer.listTransactions = listTransactions;
    }

    public int getClockLogic() {
        return clockLogic;
    }

    public LinkedList<TransactionModel> getListTransactions() {
        return listTransactions;
    }

    public Boolean finish() {
        listTransactions.removeFirst();
        return true;
    }

}
