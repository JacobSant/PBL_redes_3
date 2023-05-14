package br.uefs.pbl_redes_3.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;
@Getter
@Setter
public class TokenModel {
    private UUID clientId;
    private LocalTime time;
    private String token;
}
