package br.uefs.pbl_redes_3.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class PrivateAccountModel extends BankAccountModel{
    private UUID clientId;
}
