package br.uefs.pbl_redes_3.utils;

import org.springframework.stereotype.Component;

@Component
public class NumberAccountGenerator {
    private static int numberAccounts = 10000000;

    public synchronized int create(){
        numberAccounts++;
        return numberAccounts;
    }
}
