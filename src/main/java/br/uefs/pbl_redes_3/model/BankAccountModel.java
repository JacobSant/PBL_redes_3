package br.uefs.pbl_redes_3.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public abstract class BankAccountModel {
    private UUID id;
    private double balance;
    private int accountNumber;
    private UUID bankId;
}
