package com.example.paymentsystem.dto;

import com.example.paymentsystem.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
@AllArgsConstructor
public class HistoryDTO {
    private String username;
    private String email;
    private String fullName;
    private String address;
    private Status currentStatus;
    private Status lastStatus;
    private String executor;
    private Timestamp dateTime;

}
