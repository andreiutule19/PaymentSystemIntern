package com.example.paymentsystem.abstraction;

import com.example.paymentsystem.enums.AccountStatus;
import com.example.paymentsystem.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractAccount {
    @Id
    @Column(name ="account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    private String currency;
    private AccountStatus accountStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_status")
    private Status currentStatus;


    public AbstractAccount(String currency, AccountStatus accountStatus, Status status) {
        this.currency=currency;
        this.accountStatus=accountStatus;
        this.currentStatus=status;
    }

}
