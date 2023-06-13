package br.uefs.pbl_redes_3.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private int cpf;
    private int sourceBankId;
    private double value;
}
