package br.uefs.pbl_redes_3.utils;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Banks banks = new Banks();
        banks.initializeBanks();
        banks.getBanksReference().forEach(
                b -> System.out.println(b.getId() +"\n"+b.getPort()+"\n"+b.getIp())
        );
    }
}
