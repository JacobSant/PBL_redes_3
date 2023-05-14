package br.uefs.pbl_redes_3.model;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.UUID;

@Getter
@Setter
public class ClientModel {
    private UUID id;
    private String name;
    private String password;
    private int cpf;
    private String email;
}
