package br.uefs.pbl_redes_3.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class PaymentRequest {
    private int cpf;
    private int sourceBankId;
    private double value;
}
