package com.example.paymentsystem.history;

import com.example.paymentsystem.enums.PayHistoryStatus;
import com.example.paymentsystem.enums.PaymentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class PaymentHistory implements Comparable<PaymentHistory> {
    @Id
    @Column(name ="pay_history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payHistoryId;

    private String numberAccountA;

    private String numberAccountB;
    private Timestamp timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private PayHistoryStatus action;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status;

    private Float amount;
    private String executor;
    private String currency;
    private String userReference;
    private String systemReference;
    private String type;


    public PaymentHistory(String numberAccountA,String numberAccountB,PayHistoryStatus action,PaymentStatus status,
                          Float amount,String executor,String currency,String userReference,String systemReference,String type){
        this.numberAccountA=numberAccountA;
        this.numberAccountB =numberAccountB;
        this.action=action;
        this.status=status;
        this.amount=amount;
        this.executor=executor;
        this.currency=currency;
        this.userReference=userReference;
        this.systemReference=systemReference;
        this.type= type;
        this.timestamp=Timestamp.from(Instant.now());


    }

    @Override
    public int compareTo(PaymentHistory o) {
        return o.getSystemReference().compareTo(this.systemReference);
    }
}
