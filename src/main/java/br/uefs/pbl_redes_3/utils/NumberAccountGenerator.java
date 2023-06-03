package br.uefs.pbl_redes_3.utils;

public class NumberAccountGenerator {
    private static int numberAccounts = 10000000;

    public synchronized int create(){
        numberAccounts++;
        return numberAccounts;
    }
}
