package com.example.paymentsystem.controller;

import com.example.paymentsystem.dto.BalanceDTO;
import com.example.paymentsystem.service.sketch.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class BalanceController {

    @Autowired
    private final BalanceService balanceService;


    @GetMapping(value="/balances")
    public List<BalanceDTO> findAll(){
        return balanceService.findAll();
    }


}
