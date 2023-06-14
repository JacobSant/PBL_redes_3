package br.uefs.pbl_redes_3.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRequest {
    private int destinyBankId;
    private int sourceBankId;
    private int destinyAccountNumber;
    private String destinyAccountType;
    private int value;
    private int cpf;
}
