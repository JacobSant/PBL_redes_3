package br.uefs.pbl_redes_3.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private int destinyBankId;
    private int destinyPrivateAccountNumber;
    private int value;
}
