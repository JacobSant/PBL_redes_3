package br.uefs.pbl_redes_3.utils;

import br.uefs.pbl_redes_3.model.Bank;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Banks {

    private static final List<Bank> banksReference = new ArrayList<>();

    public void initializeBanks(){

        banksReference.add(new Bank(2,9090, "localhost"));
        banksReference.add(new Bank(1,8080, "localhost"));
    }
    public  List<Bank> getBanksReference() {
        return banksReference;
    }

}
