package com.example.paymentsystem.entity;

import com.example.paymentsystem.abstraction.AbstractAccount;
import com.example.paymentsystem.enums.AccountStatus;
import com.example.paymentsystem.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
public class Account extends AbstractAccount implements Serializable {
    @Column(name="username")
    private String username;

    @Column(name="number_account",unique = true)
    private String numberAccount;

    @OneToMany(fetch=FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name="number_account", referencedColumnName = "number_account")
    private List<Balance> balances;


    public Account(String username, String numberAccount, String currency, AccountStatus accountStatus, Status status) {
        super(currency, accountStatus, status);
        this.username = username;
        this.numberAccount = numberAccount;
        this.balances = new ArrayList<>();
        this.balances.add(new Balance());
    }


    public Balance getLastBalance(){
        return balances.get(balances.size()-1);
    }



}
