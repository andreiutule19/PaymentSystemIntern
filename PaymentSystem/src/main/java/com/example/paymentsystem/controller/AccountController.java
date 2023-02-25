package com.example.paymentsystem.controller;

import com.example.paymentsystem.dto.*;
import com.example.paymentsystem.history.AccountHistory;
import com.example.paymentsystem.service.sketch.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class AccountController {
    @Autowired
    private final AccountService accountService;


    @GetMapping(value = "/accounts/all")
    public List<AccountApproveDTO> findAll(){
        return accountService.findAll();
    }


    @PostMapping(value = "/accounts")
    public List<AccountApproveDTO> findAllByCurrency(@RequestBody AccountModifyDTO account){
        return accountService.findAllByCurrency(account);
    }

    @PostMapping(value="/accounts/insert")
    public AccountHistory insert(@RequestBody AccountModifyDTO account){
        return accountService.insert(account);
    }

    @PostMapping(value="/accounts/update")
    public AccountHistory update(@RequestBody AccountModifyDTO account){
        return accountService.update(account);
    }

    @PostMapping(value="/accounts/remove")
    public AccountHistory remove(@RequestBody AccountModifyDTO account){
        return accountService.remove(account);
    }

    @PostMapping(value="/accounts/handle")
    public void handleUser(@RequestBody AccountModifyDTO account){
        accountService.handleAccount(account);
    }

    @PostMapping("/accounts/history")
    public List<HistoryAccountDTO> mapApproveTable(){
        return accountService.mapApproveTable();
    }

    @PostMapping("/accounts/currency")
    public List<String> getCurrencies() throws ParserConfigurationException, IOException, SAXException {return  accountService.getCurrencies();}

    @PostMapping("/accounts/balance")
    public BalanceDTO getLastBalanceAccount(@RequestBody AccountModifyDTO accountModifyDTO){
        return accountService.getLastBalance(accountModifyDTO);
    }


}
