package br.uefs.pbl_redes_3.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositRequest{
    private double value;
    private int accountNumber;
}
