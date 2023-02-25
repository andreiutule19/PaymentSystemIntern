package com.example.paymentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentDTO {
    private String numberAccountA;
    private String numberAccountB;
    private Float amount;
    private String executor;
    private String currency;
    private String userReference;
    private String systemReference;
    private String type;

}
