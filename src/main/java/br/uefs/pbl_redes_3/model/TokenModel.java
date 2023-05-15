package br.uefs.pbl_redes_3.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;
@Getter
@Setter
public class TokenModel {
    private UUID clientId;
    private LocalDate issuedAt;
    private LocalDate expiresAt;
    private String token;
}
