package com.example.paymentsystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApproveDTO {
    private String username;
    private String email;
    private String fullName;
    private String address;
    private String executor;
}
