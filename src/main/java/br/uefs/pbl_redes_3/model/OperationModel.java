package br.uefs.pbl_redes_3.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter

public abstract class OperationModel {
    protected double value;
    protected Bank sourceBank;
    protected Bank destinyBank;
    protected int sourceAccountNumber;
    protected int destinyAccountNumber;
    protected LocalDateTime date;
}
