package br.uefs.pbl_redes_3.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentResponse {
    private double value;
    private LocalDateTime date;
}
