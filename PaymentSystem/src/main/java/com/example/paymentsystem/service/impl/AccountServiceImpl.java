package com.example.paymentsystem.service.impl;

import com.example.paymentsystem.dto.AccountApproveDTO;
import com.example.paymentsystem.dto.AccountModifyDTO;
import com.example.paymentsystem.dto.BalanceDTO;
import com.example.paymentsystem.dto.HistoryAccountDTO;
import com.example.paymentsystem.entity.Account;
import com.example.paymentsystem.entity.Balance;
import com.example.paymentsystem.enums.AccountStatus;
import com.example.paymentsystem.enums.Status;
import com.example.paymentsystem.exception.ResourceNotFoundException;
import com.example.paymentsystem.history.AccountHistory;
import com.example.paymentsystem.repository.AccountHistoryRepository;
import com.example.paymentsystem.repository.AccountRepository;
import com.example.paymentsystem.service.sketch.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.transaction.Transactional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountHistoryRepository accountHistoryRepository;


    private AccountHistory setAccountHistory(AccountModifyDTO account,Status lastStatus,
                                             Status currentStatus,AccountStatus accountStatus){
        return new AccountHistory(account.getUsername(),account.getNumberAccount(),lastStatus,currentStatus,
                account.getExecutor(), account.getCurrency(),accountStatus );
    }

    private Account setAccount(AccountModifyDTO accountModifyDTO,Status currentStatus){
        return new Account(accountModifyDTO.getUsername(), accountModifyDTO.getNumberAccount(),
                accountModifyDTO.getCurrency(), accountModifyDTO.getAccountStatus(),currentStatus);
    }


    private List<AccountHistory> listHistoryApprove() {
        Set<AccountHistory> historySet = new TreeSet<>(accountHistoryRepository.findAll());
        List<String> accountNumbers = historySet.stream().map(AccountHistory::getNumberAccount).toList();
        List<AccountHistory> accountHistoryList= new ArrayList<>();
        for(String numberAccount : accountNumbers){
            List<AccountHistory> accountHistories= accountHistoryRepository
                    .findAccountHistoriesByNumberAccountOrderByTimestampDesc(numberAccount);
            accountHistoryList.add(accountHistories.get(0));
        }
        return  accountHistoryList;
    }

    @Override
    @Transactional
    public AccountHistory insert(AccountModifyDTO account) {
        return accountHistoryRepository.save(this.setAccountHistory(account,null,Status.PENDING_ACTIVE,AccountStatus.OPEN));
    }

    @Override
    @Transactional
    public AccountHistory remove(AccountModifyDTO account) {
        if(!account.getCurrentStatus().equals(Status.REMOVED)) {
            return  accountHistoryRepository.save(this.setAccountHistory(account,account.getCurrentStatus(),
                    Status.PENDING_REMOVE,account.getAccountStatus()));
        }else{
            throw new ResourceNotFoundException("Account","Status",account.getCurrentStatus(), HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @Override
    @Transactional
    public AccountHistory update(AccountModifyDTO account) {
        return  accountHistoryRepository.save(this.setAccountHistory(account,account.getCurrentStatus(),
                Status.PENDING_MODIFY,account.getAccountStatus()));
    }

    @Override
    @Transactional
    public List<AccountApproveDTO> findAllByCurrency(AccountModifyDTO account) {
        return accountRepository.findAccountByCurrency(account.getCurrency())
                .stream().map(account1 -> new AccountApproveDTO(account1.getUsername(),account1.getNumberAccount(),
                        account1.getCurrency(),account1.getAccountStatus(),account1.getCurrentStatus())).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void handleAccount(AccountModifyDTO willBeApprovedAccount) {
        AccountHistory verifyAccount = null;

        for(AccountHistory y :  this.listHistoryApprove()){
            if(y.getNumberAccount().equals(willBeApprovedAccount.getNumberAccount())){
                verifyAccount = y;
                break;
            }
        }
        if (verifyAccount != null) {
            if(verifyAccount.getExecutor().equals(willBeApprovedAccount.getExecutor()) || willBeApprovedAccount.getExecutor().equals(willBeApprovedAccount.getUsername())){
                throw new ResourceNotFoundException("Account","AccountNumber",willBeApprovedAccount.getAccountStatus(), HttpStatus.NOT_ACCEPTABLE);
            }
            else {
                if (verifyAccount.getCurrentStatus().equals(Status.PENDING_ACTIVE)) {
                    accountHistoryRepository.save(
                            this.setAccountHistory(willBeApprovedAccount,Status.PENDING_ACTIVE,Status.ACTIVE,willBeApprovedAccount.getAccountStatus())
                    );
                    accountRepository.save(this.setAccount(willBeApprovedAccount,Status.ACTIVE));
                }
                if (verifyAccount.getCurrentStatus().equals(Status.PENDING_MODIFY)) {
                    Account newAccount = accountRepository.findAccountByNumberAccount(verifyAccount.getNumberAccount());
                    newAccount.setCurrentStatus(Status.MODIFIED);
                    newAccount.setUsername(willBeApprovedAccount.getUsername());
                    newAccount.setAccountStatus(willBeApprovedAccount.getAccountStatus());
                    newAccount.setCurrency(willBeApprovedAccount.getCurrency());
                    accountHistoryRepository.save(
                            this.setAccountHistory(willBeApprovedAccount,Status.PENDING_MODIFY,Status.MODIFIED,willBeApprovedAccount.getAccountStatus())
                    );
                    accountRepository.save(newAccount);
                }
                if (verifyAccount.getCurrentStatus().equals(Status.PENDING_REMOVE)) {
                    Account newAccount = accountRepository.findAccountByNumberAccount(verifyAccount.getNumberAccount());
                    newAccount.setCurrentStatus(Status.REMOVED);
                    accountHistoryRepository.save(
                            this.setAccountHistory(willBeApprovedAccount,Status.PENDING_MODIFY,Status.MODIFIED,willBeApprovedAccount.getAccountStatus())
                    );
                    accountRepository.save(newAccount);
                }
            }
        }


    }

    @Override
    @Transactional
    public List<HistoryAccountDTO> mapApproveTable() {

        return this.listHistoryApprove().stream().map(history ->
                new HistoryAccountDTO(history.getUsername(),history.getNumberAccount(),
                        history.getCurrency(),history.getAccountStatus(),history.getCurrentStatus(),
                        history.getLastStatus(),history.getExecutor())).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<String> getCurrencies() throws ParserConfigurationException, IOException, SAXException {

        List<String> currencies = new ArrayList<>();

        File file = new File(
                "/Users/iuliuandreisteau/Desktop/INTERNSHIP/PaymentSystem/currency.xml");
        DocumentBuilderFactory dbf
                = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);

        doc.getDocumentElement().normalize();
        System.out.println(
                "Root element: "
                        + doc.getDocumentElement().getNodeName());
        NodeList nodeList
                = doc.getElementsByTagName("currency");
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);
            if (node.getNodeType()
                    == Node.ELEMENT_NODE) {
                Element tElement = (Element) node;
                currencies.add(tElement
                        .getElementsByTagName("code")
                        .item(0)
                        .getTextContent());
            }
        }
        return currencies;
    }

    @Override
    @Transactional
    public List<AccountApproveDTO> findAll() {
        return accountRepository.findAll().stream().map(account ->
                new AccountApproveDTO(account.getUsername(),account.getNumberAccount(),
                        account.getCurrency(),account.getAccountStatus(),
                        account.getCurrentStatus())).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BalanceDTO getLastBalance(AccountModifyDTO accountModifyDTO) {
        Balance balance= accountRepository.findAccountByNumberAccount(accountModifyDTO.getNumberAccount()).getLastBalance();
        return new BalanceDTO(balance.getNumberAccount(),
                balance.getAvailableCreditAmount(),
                balance.getAvailableCreditCount(),
                balance.getAvailableDebitAmount(),
                balance.getAvailableDebitCount(),
                balance.getPendingCreditAmount(),
                balance.getPendingDebitAmount(),
                balance.getPendingCreditCount(),
                balance.getPendingDebitCount()
                );
    }


}
