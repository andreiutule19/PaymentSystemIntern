package com.example.paymentsystem.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Table(name = "balance")
@Getter
@Setter
public class Balance implements Serializable {
    @Id
    @Column(name ="balance_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long balanceId;
    private LocalDateTime dateTime;

    @Column(name="number_account")
    private String numberAccount;

    @Setter(AccessLevel.NONE)
    private float availableBalance;
    private float availableCreditAmount;
    private float availableCreditCount;
    private float availableDebitAmount;
    private float availableDebitCount;
    private float pendingCreditAmount;
    private float pendingDebitAmount;
    private float pendingCreditCount;
    private float pendingDebitCount;


    public Balance (float availableCreditAmount,float pendingCreditAmount,float availableDebitAmount,float pendingDebitAmount,
                    float pendingCreditCount,float pendingDebitCount,float availableCreditCount,float availableDebitCount){
        this.availableCreditAmount= availableCreditAmount;
        this.availableDebitAmount= availableDebitAmount;
        this.pendingCreditAmount = pendingCreditAmount;
        this.pendingDebitAmount = pendingDebitAmount;
        this.availableBalance= availableCreditAmount-availableDebitAmount;
        this.pendingCreditCount=pendingCreditCount;
        this.pendingDebitCount=pendingDebitCount;
        this.availableCreditCount=availableCreditCount;
        this.availableDebitCount = availableDebitCount;
        this.dateTime=LocalDateTime.now();

    }

    public Balance(){
        this.dateTime=LocalDateTime.now();
    }



}
