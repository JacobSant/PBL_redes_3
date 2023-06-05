package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.model.TransactionModel;
import br.uefs.pbl_redes_3.utils.OtherBanks;
import br.uefs.pbl_redes_3.utils.Synchronizer;
import org.springframework.stereotype.Service;

@Service
public class ReceiveAckService {
    private final Synchronizer synchronizer;

    public ReceiveAckService(Synchronizer synchronizer) {
        this.synchronizer = synchronizer;
    }
    public void receivedACK(TransactionModel transaction){
        synchronizer.getListTransactions().forEach(t ->{
            if(t.getIdTransaction() == transaction.getIdTransaction()){
                t.setAck(t.getAck()+1);
            }
            if(t.getAck() == OtherBanks.getBanksReference().size()){
                synchronizer.getListTransactions().remove(t);
            }
        });
    }

}
