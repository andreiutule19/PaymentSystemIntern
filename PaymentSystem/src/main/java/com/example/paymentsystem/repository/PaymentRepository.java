package com.example.paymentsystem.repository;

import com.example.paymentsystem.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

    @Override
    Payment save(Payment payment);
}
