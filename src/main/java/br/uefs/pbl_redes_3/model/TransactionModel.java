package br.uefs.pbl_redes_3.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter

public abstract class TransactionModel {
    protected double value;
    protected BankReferenceModel destinyBank;
    protected ClientReferenceModel sourceClient;
    protected ClientReferenceModel destinyClient;
    protected LocalDateTime date;
}
