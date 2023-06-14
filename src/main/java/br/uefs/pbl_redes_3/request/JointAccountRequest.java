package br.uefs.pbl_redes_3.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class JointAccountRequest {
    private String clientId1;
    private String clientId2;
}
