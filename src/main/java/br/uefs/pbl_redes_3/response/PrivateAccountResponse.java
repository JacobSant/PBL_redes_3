package br.uefs.pbl_redes_3.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class PrivateAccountResponse {
    private UUID id;
    private int accountNumber;
    private double balance;
    private UUID clientId;
}
