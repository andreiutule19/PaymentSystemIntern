package com.example.paymentsystem.dto;

import com.example.paymentsystem.enums.PayHistoryStatus;
import com.example.paymentsystem.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PaymentHistoryDTO {
    private String numberAccountA;
    private String numberAccountB;
    private Float amount;
    private String executor;
    private String currency;
    private String userReference;
    private String systemReference;
    private String type;
    private PaymentStatus status;
    private PayHistoryStatus action;
}
