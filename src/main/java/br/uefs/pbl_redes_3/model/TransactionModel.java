package br.uefs.pbl_redes_3.model;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter

public abstract class TransactionModel {
    private double value;
    private BankReferenceModel destinyBank;
    private ClientReferenceModel sourceClient;
    private ClientReferenceModel destinyClient;
    private LocalDateTime date;
}
