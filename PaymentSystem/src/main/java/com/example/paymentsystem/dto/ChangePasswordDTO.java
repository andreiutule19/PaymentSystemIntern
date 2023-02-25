package com.example.paymentsystem.dto;

import lombok.Getter;

@Getter
public class ChangePasswordDTO {
    private String oldPassword;
    private String password;
    private String executor;
}
