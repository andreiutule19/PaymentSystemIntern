package com.example.paymentsystem.entity;

import com.example.paymentsystem.abstraction.AbstractUsers;
import com.example.paymentsystem.enums.Status;
import com.example.paymentsystem.history.PaymentHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "user")
@NoArgsConstructor
@Getter
@Setter
public class User extends AbstractUsers implements Serializable {
    @Column(name="username",unique = true)
    private String username;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name="username",referencedColumnName = "username",insertable = false, updatable = false)
    List<Account> accountList;

    public User(String username, String email, String password, String fullName, String address, Status currentStatus) {
        super(email, password, fullName, address, currentStatus);
        this.username=username;

    }


}
