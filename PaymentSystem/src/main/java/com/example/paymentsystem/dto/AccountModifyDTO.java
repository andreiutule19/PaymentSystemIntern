package com.example.paymentsystem.dto;

import com.example.paymentsystem.enums.AccountStatus;
import com.example.paymentsystem.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccountModifyDTO {
    private String username;
    private String numberAccount;
    private String currency;
    private String executor;
    private AccountStatus accountStatus;
    private Status currentStatus;
}
