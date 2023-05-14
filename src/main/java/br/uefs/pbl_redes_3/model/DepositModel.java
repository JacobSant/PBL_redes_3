package br.uefs.pbl_redes_3.model;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DepositModel {

    private double value;
    private BankReferenceModel sourceBank;
    private BankReferenceModel destinyBank;
    private ClientReferenceModel sourceClient;
    private ClientReferenceModel destinyClient;
    private LocalDateTime date;
}
