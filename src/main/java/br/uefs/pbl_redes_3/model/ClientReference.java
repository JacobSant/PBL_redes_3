package br.uefs.pbl_redes_3.model;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ClientReference {
    private String name;
    private UUID ClientId;
}
