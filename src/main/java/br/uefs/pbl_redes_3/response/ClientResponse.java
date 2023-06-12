package br.uefs.pbl_redes_3.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class ClientResponse {
    private UUID id;
    private String name;
    private String email;
    private double balance;
}
