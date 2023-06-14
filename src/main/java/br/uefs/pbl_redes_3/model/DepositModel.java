package br.uefs.pbl_redes_3.model;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class DepositModel {
    private UUID id;
    private UUID accountId;
    private double value;
    private LocalDateTime date;
}

