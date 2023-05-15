package br.uefs.pbl_redes_3.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


public class NumberAccountGenerator {
    private static int numberAccounts = 10000000;

    public synchronized int create(){
        numberAccounts++;
        return numberAccounts;
    }
}
