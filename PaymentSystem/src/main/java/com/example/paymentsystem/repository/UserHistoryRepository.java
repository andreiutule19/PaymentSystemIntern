package com.example.paymentsystem.repository;

import com.example.paymentsystem.entity.User;
import com.example.paymentsystem.history.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface UserHistoryRepository extends JpaRepository<UserHistory,Long> {
    @Override
    UserHistory save(UserHistory userHistory);

    @Override
    List<UserHistory> findAll();

    UserHistory findUserHistoryByUsernameAndEmail(String username,String email);

    List<UserHistory> findUserHistoriesByUsernameOrderByTimestampDesc(String username);



}
