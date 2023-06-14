package br.uefs.pbl_redes_3.model;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class TransfersModel {
    private UUID id;
    private double value;
    private int sourceBankId;
    private int destinyBankId;
    private int sourceAccountNumber;
    private int destinyAccountNumber;
    private LocalDateTime date;
}
