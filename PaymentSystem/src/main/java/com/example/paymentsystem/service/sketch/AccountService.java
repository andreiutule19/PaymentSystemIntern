package com.example.paymentsystem.service.sketch;

import com.example.paymentsystem.dto.*;
import com.example.paymentsystem.entity.Balance;
import com.example.paymentsystem.history.AccountHistory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public interface AccountService {
    AccountHistory insert(AccountModifyDTO account);

    AccountHistory remove(AccountModifyDTO account);
    AccountHistory update(AccountModifyDTO  account);

    List<AccountApproveDTO > findAllByCurrency(AccountModifyDTO accountModifyDTO);

    void handleAccount(AccountModifyDTO account);

    List<HistoryAccountDTO> mapApproveTable();

    List<String> getCurrencies() throws ParserConfigurationException, IOException, SAXException;

    List<AccountApproveDTO> findAll();


    BalanceDTO getLastBalance(AccountModifyDTO accountModifyDTO);


}
