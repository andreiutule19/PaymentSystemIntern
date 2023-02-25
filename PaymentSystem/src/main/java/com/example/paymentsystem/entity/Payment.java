package com.example.paymentsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Payment implements Serializable {
    @Id
    @Column(name ="payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    private String type;
    private Float debitAmountA;
    private Float creditAmountB;
    private LocalDateTime dateTime;
    private Float amount;
    private String currency;
    private String userReference;

    private String sender;
    private String receiver;
    @Column(name="system_reference",unique = true)
    private String systemReference;

    public Payment (String type,Float debitAmountA,Float creditAmountB,
                    Float amount ,String currency, String userReference,String systemReference,String receiver,String sender){
        this.type=type;
        this.debitAmountA=debitAmountA;
        this.creditAmountB=creditAmountB;
        this.dateTime=LocalDateTime.now();
        this.receiver= receiver;
        this.sender=sender;
        this.amount=amount;
        this.currency=currency;
        this.userReference=userReference;
        this.systemReference =systemReference;

    }

}
