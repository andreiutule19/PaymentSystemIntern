package com.example.paymentsystem.dto;

import com.example.paymentsystem.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private String username;
    private String email;
    private String fullName;
    private String address;
    private Status status;
    private String password;
    private String executor;
}
