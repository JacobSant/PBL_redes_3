package br.uefs.pbl_redes_3.utils;

import br.uefs.pbl_redes_3.model.Bank;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class OtherBanks {
    private static List<Bank> banksReference;

    public void setBanks(){
        banksReference.add(new Bank(0001,2000, "localhost"));
        banksReference.add(new Bank(0002,3000, "localhost"));
    }
    public synchronized static List<Bank> getBanksReference() {
        return banksReference;
    }

}
