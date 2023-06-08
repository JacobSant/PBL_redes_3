package br.uefs.pbl_redes_3.utils;

import br.uefs.pbl_redes_3.model.Bank;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class Banks {
    private static final List<Bank> banksReference = new ArrayList<>();

    public void initializeBanks(){
        banksReference.add(new Bank(1,2000, "localhost"));
        banksReference.add(new Bank(2,3000, "localhost"));
    }
    public  List<Bank> getBanksReference() {
        return banksReference;
    }

}
