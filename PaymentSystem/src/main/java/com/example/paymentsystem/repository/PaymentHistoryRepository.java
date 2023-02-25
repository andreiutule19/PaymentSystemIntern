package com.example.paymentsystem.repository;

import com.example.paymentsystem.history.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory,Long> {

    @Override
    PaymentHistory save(PaymentHistory paymentHistory);

    List<PaymentHistory> findPaymentHistoriesByNumberAccountAOrderByTimestampDesc(String usernameA);

    @Override
    List<PaymentHistory> findAll();

    List<PaymentHistory> findPaymentHistoriesBySystemReferenceOrderByTimestampDesc(String systemReference);




}
