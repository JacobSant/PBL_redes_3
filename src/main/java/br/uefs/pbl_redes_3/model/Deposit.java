package br.uefs.pbl_redes_3.model;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class Deposit {

    private double value;
    private BankReference sourceBank;
    private BankReference destinyBank;
    private ClientReference sourceClient;
    private ClientReference destinyClient;
    private LocalDateTime date;
}
