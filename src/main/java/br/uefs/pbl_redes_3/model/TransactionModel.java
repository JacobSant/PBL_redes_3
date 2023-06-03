package br.uefs.pbl_redes_3.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionModel extends OperationModel{
    private int requestTime;
    private boolean freeForExecution;
}
