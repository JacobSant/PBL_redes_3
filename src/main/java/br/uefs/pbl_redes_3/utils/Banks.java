package br.uefs.pbl_redes_3.utils;

import br.uefs.pbl_redes_3.model.Bank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Banks {
    private static final List<Bank> banksReference = new ArrayList<>();
    @Value("${bank.port}")
    private int port;
    @Value("${bank.host}")
    private String host;
    @Value("${bank.id}")
    private int bankId;

    public void initializeBanks(){
        banksReference.add(new Bank(1,8080, "localhost"));
        System.out.println(bankId+"\n"+port+"\n"+host);
    }
    public  List<Bank> getBanksReference() {
        return banksReference;
    }

}
