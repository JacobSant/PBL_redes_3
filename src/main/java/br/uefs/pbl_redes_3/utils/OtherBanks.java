package br.uefs.pbl_redes_3.utils;

import java.util.HashMap;
import java.util.Map;

public class OtherBanks {
    private static Map<Integer,Integer> banksReference = new HashMap<>();

    public synchronized static Map<Integer, Integer> getBanksReference() {
        return banksReference;
    }

}
