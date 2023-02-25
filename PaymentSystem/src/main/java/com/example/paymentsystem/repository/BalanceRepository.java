package com.example.paymentsystem.repository;

import com.example.paymentsystem.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BalanceRepository extends JpaRepository<Balance,Long> {

    @Override
    Balance save(Balance balance);

    @Override
    List<Balance> findAll();
}
