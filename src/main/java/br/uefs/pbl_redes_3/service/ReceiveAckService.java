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

    public void receivedACK(TransactionModel transaction) {
        System.out.println(synchronizer.getListTransactions().size() + "Receive Ack Service linha 19");
        if (!synchronizer.getListTransactions().isEmpty()) {

            synchronizer.getListTransactions().forEach(t -> {
                if (t.getIdTransaction().equals(transaction.getIdTransaction())) {
                    t.setAck(t.getAck() + 1);
                }
            });
            System.out.println(banks.getBanksReference().size() + "   ReceiveAck linha 25");
            System.out.println(synchronizer.getListTransactions().getFirst().getAck() + "    synchronizer.getListTransactions().getFirst().getAck()");
            if (synchronizer.getListTransactions().getFirst().getAck() == banks.getBanksReference().size()) {

                synchronizer.getListTransactions().getFirst().setExecutable(true);
            }
        }
    }
}
