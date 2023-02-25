package com.example.paymentsystem.dto;

import com.example.paymentsystem.enums.AccountStatus;
import com.example.paymentsystem.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccountApproveDTO {
    private String username;
    private String numberAccount;
    private String currency;
    private AccountStatus accountStatus;
    private Status currentStatus;



}
