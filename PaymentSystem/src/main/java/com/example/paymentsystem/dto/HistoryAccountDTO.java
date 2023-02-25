package com.example.paymentsystem.dto;

import com.example.paymentsystem.enums.AccountStatus;
import com.example.paymentsystem.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class HistoryAccountDTO {
    private String username;
    private String numberAccount;
    private String currency;
    private AccountStatus accountStatus;
    private Status currentStatus;
    private Status lastStatus;
    private String executor;


}
