package br.uefs.pbl_redes_3.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;
@Getter
@Setter
public class TokenModel {
    private UUID clientId;
    private Date issuedAt;
    private long expiresAt;
    private String token;
}
