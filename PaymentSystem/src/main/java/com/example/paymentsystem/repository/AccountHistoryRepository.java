package com.example.paymentsystem.repository;

import com.example.paymentsystem.history.AccountHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface AccountHistoryRepository extends JpaRepository<AccountHistory,Long> {
    @Override
    AccountHistory save(AccountHistory accountHistory);

    @Override
    List<AccountHistory> findAll();

    List<AccountHistory> findAccountHistoriesByNumberAccountOrderByTimestampDesc(String numberAccount);
}
