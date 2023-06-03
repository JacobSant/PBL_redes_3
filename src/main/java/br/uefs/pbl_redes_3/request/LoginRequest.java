package br.uefs.pbl_redes_3.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String cpf;
    private String password;
    private String numberAccount;
}
