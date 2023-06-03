package br.uefs.pbl_redes_3.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
public class Bank {
    private int id;
    private int port;
    private String ip;
}
