package br.uefs.pbl_redes_3.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientRequest {
    private String name;
    private String password;
    private int cpf;
    private String email;
}
