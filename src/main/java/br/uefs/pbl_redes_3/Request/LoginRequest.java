package br.uefs.pbl_redes_3.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String username;
    private String password;
    private String numberAccount;

}
