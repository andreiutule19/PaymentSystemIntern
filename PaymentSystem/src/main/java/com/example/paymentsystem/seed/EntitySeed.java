package com.example.paymentsystem.seed;

import com.example.paymentsystem.entity.Account;
import com.example.paymentsystem.entity.Balance;
import com.example.paymentsystem.entity.User;
import com.example.paymentsystem.enums.AccountStatus;
import com.example.paymentsystem.enums.Status;
import com.example.paymentsystem.history.UserHistory;
import com.example.paymentsystem.repository.AccountRepository;
import com.example.paymentsystem.repository.BalanceRepository;
import com.example.paymentsystem.repository.UserHistoryRepository;
import com.example.paymentsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
// The Order ensures that this CommandLineRunner is ran first (before the ConsoleController if you implement that one too)

@Order(Ordered.HIGHEST_PRECEDENCE)
public class EntitySeed implements CommandLineRunner {

    private final UserRepository factory;
    private final UserHistoryRepository factoryRepo;

    private final AccountRepository repository;

    private final BalanceRepository balanceRepository;

    @Override
    @Transactional
    public void run(String... args) {

        if(factory.findAll().isEmpty()) {
            User user = new User("andreiutule19","andrei_steau@yahoo.com",new BCryptPasswordEncoder().encode("password"),"Steau Iuliu Andrei","Str Princip Galda de Jos nr 460", Status.ACTIVE);
            User user1 = new User("andreiutule199","andrei_steau2@yahoo.com",new BCryptPasswordEncoder().encode("password2"),"Steau Iuliu Andrei","Str Princip Galda de Jos nr 460", Status.ACTIVE);
            UserHistory userHistory= new UserHistory(
                    "andrei_steau2@yahoo.com",null,"Steau Iuliu","ceva",
                    Status.PENDING_MODIFY,Status.ACTIVE,"andreiutule199","andreiutule199"
            );
            UserHistory userHistory2= new UserHistory(
                    user.getEmail(),null,"Steau Iuliu Andrew",user.getAddress(),
                    Status.PENDING_MODIFY,Status.ACTIVE,user.getUsername(),user.getUsername()
            );
            UserHistory userHistory3= new UserHistory(
                    user.getEmail(),null,"Steau Iuliu Andre","ceva nou",
                    Status.PENDING_MODIFY,Status.ACTIVE,user.getUsername(),user.getUsername()
            );

            factoryRepo.save(userHistory);
            factoryRepo.save(userHistory2);
            factoryRepo.save(userHistory3);
            factory.save(user);
            factory.save(user1);
            Account account1 = new Account(
                    "andreiutule19","RO04812719847122steau","RON",
                    AccountStatus.OPEN,Status.ACTIVE
            );
            Account account2 = new Account(
                    "andreiutule199","EU04712719847124steau","EUR",
                    AccountStatus.OPEN,Status.ACTIVE);
            Account account3 = new Account(
                    "andreiutule19","US04812719847122steau","USD",
                    AccountStatus.OPEN,Status.ACTIVE);
            Balance balance = new Balance(
                    1000,0,200,0,
                    0,0,1,1);
            balance.setNumberAccount(account1.getNumberAccount());

            repository.save(account1);
            repository.save(account2);
            repository.save(account3);
            balanceRepository.save(balance);

        }

    }
}
