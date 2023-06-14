package br.uefs.pbl_redes_3.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransferResponse {
    private double value;
    private int sourceBankId;
    private int destinyBankId;
    private LocalDateTime date;
}
