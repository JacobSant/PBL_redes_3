package br.uefs.pbl_redes_3.service;

import br.uefs.pbl_redes_3.utils.Synchronizer;
import org.springframework.stereotype.Service;

@Service
public class FinishTransactionService {
    private final Synchronizer synchronizer;

    public FinishTransactionService(Synchronizer synchronizer) {
        this.synchronizer = synchronizer;
    }


    public Boolean finish(){
        return synchronizer.finish();
    }
}
