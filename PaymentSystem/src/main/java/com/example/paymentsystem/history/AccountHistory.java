package com.example.paymentsystem.history;

import com.example.paymentsystem.abstraction.AbstractAccount;
import com.example.paymentsystem.enums.AccountStatus;
import com.example.paymentsystem.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AccountHistory extends AbstractAccount implements Comparable<AccountHistory>{
    private String username;
    private Timestamp timestamp;
    private String numberAccount;

    @Enumerated(EnumType.STRING)
    @Column(name = "last_status")
    private Status lastStatus;

    private String executor;


    public AccountHistory(String username, String numberAccount, Status lastStatus,Status currentStatus,
                          String executor, String currency, AccountStatus accountStatus){
        super(currency,accountStatus,currentStatus);
        this.username=username;
        this.numberAccount=numberAccount;
        this.lastStatus=lastStatus;
        this.executor=executor;
        this.timestamp=Timestamp.from(Instant.now());

    }


    @Override
    public int compareTo(AccountHistory o) {
        return o.getNumberAccount().compareTo(this.numberAccount);
    }
}
