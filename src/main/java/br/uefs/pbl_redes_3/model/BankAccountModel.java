package br.uefs.pbl_redes_3.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public abstract class BankAccountModel {
    protected UUID id;
    protected double balance;
    protected int accountNumber;
    protected UUID bankId;
}