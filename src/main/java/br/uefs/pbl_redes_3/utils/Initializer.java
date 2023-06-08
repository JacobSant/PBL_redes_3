package br.uefs.pbl_redes_3.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements ApplicationRunner {
    @Value("${bank.id}")
    private int bankId;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Banks banks = new Banks();
        banks.initializeBanks();
        banks.getBanksReference().forEach(
                b -> System.out.println(b.getId())
        );
        System.out.println(bankId);
    }
}
