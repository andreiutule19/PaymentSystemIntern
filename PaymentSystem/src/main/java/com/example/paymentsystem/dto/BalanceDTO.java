package com.example.paymentsystem.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BalanceDTO {
    private String numberAccount;
    private float availableBalance;
    private float availableCreditAmount;
    private float availableCreditCount;
    private float availableDebitAmount;
    private float availableDebitCount;
    private float pendingCreditAmount;
    private float pendingDebitAmount;
    private float pendingCreditCount;
    private float pendingDebitCount;

    public BalanceDTO(String numberAccount,float aca,float acc,float ada,float adc,float pca, float pda,float pcc,float pdc){
        this.numberAccount=numberAccount;
        this.availableCreditAmount=aca;
        this.availableDebitAmount=ada;
        this.availableBalance= this.availableCreditAmount- this.availableDebitAmount;
        this.availableCreditCount=acc;
        this.availableDebitCount=adc;
        this.pendingCreditAmount=pca;
        this.pendingDebitAmount=pda;
        this.pendingCreditCount=pcc;
        this.pendingDebitCount=pdc;
    }
}
