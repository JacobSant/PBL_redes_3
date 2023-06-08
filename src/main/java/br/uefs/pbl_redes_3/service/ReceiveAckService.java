package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.model.TransactionModel;
import br.uefs.pbl_redes_3.utils.Banks;
import br.uefs.pbl_redes_3.utils.Synchronizer;
import org.springframework.stereotype.Service;

@Service
public class ReceiveAckService {
    private final Synchronizer synchronizer;
    private final Banks banks;

    public ReceiveAckService(Synchronizer synchronizer, Banks banks) {
        this.synchronizer = synchronizer;
        this.banks = banks;
    }
    public void receivedACK(TransactionModel transaction){
        synchronizer.getListTransactions().forEach(t ->{
            if(t.getIdTransaction() == transaction.getIdTransaction()){
                t.setAck(t.getAck()+1);
            }
        });

        if(synchronizer.getListTransactions().getFirst().getAck() == banks.getBanksReference().size()){
            synchronizer.getListTransactions().removeFirst();
        }
    }

}
