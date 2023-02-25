package com.example.paymentsystem.repository;

import com.example.paymentsystem.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface AccountRepository extends JpaRepository<Account,Long> {
    @Override
    Account save(Account account);

    List<Account> findAccountByCurrency(String currency);

    Account findAccountByNumberAccount(String numberAccount);

    @Override
    List<Account> findAll();

}
