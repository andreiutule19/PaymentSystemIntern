package com.example.paymentsystem.service.impl;

import com.example.paymentsystem.dto.BalanceDTO;
import com.example.paymentsystem.repository.BalanceRepository;
import com.example.paymentsystem.service.sketch.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService{
    @Autowired
    private BalanceRepository balanceRepository;


    @Override
    public List<BalanceDTO> findAll() {
        return balanceRepository.findAll().stream().map(balance ->
                new BalanceDTO(
                        balance.getNumberAccount(),
                        balance.getAvailableCreditAmount(),
                        balance.getAvailableCreditCount(),
                        balance.getAvailableDebitAmount(),
                        balance.getAvailableDebitCount(),
                        balance.getPendingCreditAmount(),
                        balance.getPendingDebitAmount(),
                        balance.getPendingCreditCount(),
                        balance.getPendingDebitCount()
                )
        ).collect(Collectors.toList());
    }
}
