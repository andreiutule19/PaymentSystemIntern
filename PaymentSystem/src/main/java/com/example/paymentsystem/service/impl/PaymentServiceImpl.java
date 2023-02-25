package com.example.paymentsystem.service.impl;

import com.example.paymentsystem.dto.ExternalPaymentDTO;
import com.example.paymentsystem.dto.PaymentDTO;
import com.example.paymentsystem.dto.PaymentHistoryDTO;
import com.example.paymentsystem.entity.Account;
import com.example.paymentsystem.entity.Balance;
import com.example.paymentsystem.entity.Payment;
import com.example.paymentsystem.enums.AccountStatus;
import com.example.paymentsystem.enums.PayHistoryStatus;
import com.example.paymentsystem.enums.PaymentStatus;
import com.example.paymentsystem.exception.ResourceNotFoundException;
import com.example.paymentsystem.history.PaymentHistory;
import com.example.paymentsystem.parser.ModifyXmlDomParser;
import com.example.paymentsystem.repository.AccountRepository;
import com.example.paymentsystem.repository.BalanceRepository;
import com.example.paymentsystem.repository.PaymentHistoryRepository;
import com.example.paymentsystem.repository.PaymentRepository;
import com.example.paymentsystem.service.sketch.PaymentService;
import okhttp3.*;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private AccountRepository accountRepository;
    private final OkHttpClient client = new OkHttpClient();
//    private List<Float> ronRates;
//
//    {
//        try {
//            ronRates = this.getRates("ron-rate");
//        } catch (ParserConfigurationException | SAXException | IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    private final List<Float> usdRates;
//
//    {
//        try {
//            usdRates = this.getRates("usd-rate");
//        } catch (ParserConfigurationException | SAXException | IOException e) {
//            throw new RuntimeException(e);
//        }
//    }


//
//    private final List<Float> eurRates;
//
//    {
//        try {
//            eurRates = this.getRates("eur-rate");
//        } catch (ParserConfigurationException | SAXException | IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    private Float chooseRate(String currencyA,String currencyB){
//        if(currencyA.equals(currencyB)) return 1F;
//        switch(currencyB){
//            case "RON":
//                if(currencyA.equals("EUR")){
//                    return ronRates.get(1);
//                }
//                else  return ronRates.get(2);
//            case "EUR":
//                if(currencyA.equals("RON")){
//                    return eurRates.get(0);
//                }
//                else  return eurRates.get(2);
//            case "USD" :
//                if(currencyA.equals("RON")){
//                    return usdRates.get(0);
//                }
//                else  return usdRates.get(1);
//        }
//        return null;
//    }


    private List<Float> getRates(String rateName) throws ParserConfigurationException, IOException, SAXException{
        List<Float> rates = new ArrayList<>();

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
            System.out.println("\nNode Name :"
                    + node.getNodeName());
            if (node.getNodeType()
                    == Node.ELEMENT_NODE) {
                Element tElement = (Element) node;
                rates.add(Float.parseFloat(tElement
                        .getElementsByTagName(rateName)
                        .item(0)
                        .getTextContent()));
            }
        }
        return rates;
    }

    private PaymentHistory completePayment(PaymentDTO payment) {
        Float debitAmountA=this.getLastBalancePayment(payment.getNumberAccountA()).getAvailableDebitAmount();
        Float creditAmountB=this.getLastBalancePayment(payment.getNumberAccountB()).getAvailableCreditAmount();
        Payment completePayment= new Payment(
                payment.getType(),debitAmountA,creditAmountB, payment.getAmount(),
                payment.getCurrency(), payment.getUserReference(), payment.getSystemReference(),payment.getNumberAccountB(), payment.getNumberAccountA()
        );
//        balanceRepository.save(this.setAvailableBalance(this.getLastBalancePayment
//                (payment.getNumberAccountA()), payment.getAmount()*this.chooseRate(accountRepository.findAccountByNumberAccount(payment.getNumberAccountA()).getCurrency(), payment.getCurrency()), false,payment.getNumberAccountA())
//        );
//        if(payment.getType().equals("Internal")) {
//            balanceRepository.save(this.setAvailableBalance(this.getLastBalancePayment
//                    (payment.getNumberAccountB()), payment.getAmount()*this.chooseRate(accountRepository.findAccountByNumberAccount(payment.getNumberAccountB()).getCurrency(), payment.getCurrency()), true, payment.getNumberAccountB())
//            );
//        }
        paymentRepository.save(completePayment);

        return  paymentHistoryRepository.save(this.setPaymentHistory(payment,
                PayHistoryStatus.COMPLETED, null,payment.getSystemReference()));
    }


    private String generateSystemReference(){
        return new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS", Locale.ENGLISH).format(new Date());
    }
    private Balance getLastBalancePayment(String numberAccount){
        Account account = accountRepository.findAccountByNumberAccount(numberAccount);
        return account.getLastBalance();
    }

    private void checkPayment(PaymentDTO payment, Boolean checkNoFraud){
        PaymentHistory paymentHistory = paymentHistoryRepository.findPaymentHistoriesBySystemReferenceOrderByTimestampDesc(payment.getSystemReference()).get(0);

        if(checkNoFraud){
            if(!paymentHistory.getAction().equals(PayHistoryStatus.VERIFIED) && !paymentHistory.getAction().equals(PayHistoryStatus.APPROVED)){
                System.out.println("aci ii o pb");
                throw new ResourceNotFoundException("Payment","executor",payment.getExecutor(), HttpStatus.METHOD_NOT_ALLOWED);
            }
        }

        String executor =paymentHistory.getExecutor();
        if(payment.getExecutor().equals(executor)){
            throw new ResourceNotFoundException("Payment","executor",payment.getExecutor(), HttpStatus.UNAUTHORIZED);
        }

        if(payment.getType().equals("Internal")){
            if(payment.getExecutor().equals(accountRepository.findAccountByNumberAccount(payment.getNumberAccountA()).getUsername())
                    || payment.getExecutor().equals(accountRepository.findAccountByNumberAccount(payment.getNumberAccountB()).getUsername())) {
                throw new ResourceNotFoundException("Payment","executor",payment.getExecutor(), HttpStatus.NOT_ACCEPTABLE);
            }
        }


        if(payment.getType().equals("External")){
            if (payment.getExecutor().equals(accountRepository.findAccountByNumberAccount(payment.getNumberAccountA()).getUsername())) {
                throw new ResourceNotFoundException("Payment", "executor", payment.getExecutor(), HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }



    private void checkInsertedPayment(PaymentDTO payment){

        if(accountRepository.findAccountByNumberAccount(payment.getNumberAccountA()) != null && accountRepository.findAccountByNumberAccount(payment.getNumberAccountB()) != null) {
            AccountStatus A = accountRepository.findAccountByNumberAccount(payment.getNumberAccountA()).getAccountStatus();
            AccountStatus B = accountRepository.findAccountByNumberAccount(payment.getNumberAccountB()).getAccountStatus();
            if (A.equals(AccountStatus.BLOCKED) || A.equals(AccountStatus.CLOSED) || A.equals(AccountStatus.BLOCK_DEBIT)
                    || B.equals(AccountStatus.BLOCKED) || B.equals(AccountStatus.CLOSED) || B.equals(AccountStatus.BLOCK_CREDIT))
                throw new ResourceNotFoundException("Payment", "executor", payment.getExecutor(), HttpStatus.NOT_ACCEPTABLE);
        }
        else throw new ResourceNotFoundException("Payment", "executor", payment.getExecutor(), HttpStatus.NOT_FOUND);

    }



    private Balance setAvailableBalance(Balance lastBalance, Float amount, Boolean debitOrCredit,String numberAccount){


        Balance balance ;
        if(debitOrCredit) {
            balance= new Balance(
                    lastBalance.getAvailableCreditAmount()+amount,
                    lastBalance.getPendingCreditAmount()-amount,
                    lastBalance.getAvailableDebitAmount(),
                    lastBalance.getPendingDebitAmount(),
                    lastBalance.getPendingCreditCount()-1,
                    lastBalance.getPendingDebitCount(),
                    lastBalance.getAvailableCreditCount()+1,
                    lastBalance.getAvailableDebitCount()
            );
        }else{
            balance = new Balance(
                    lastBalance.getAvailableCreditAmount(),
                    lastBalance.getPendingCreditAmount(),
                    lastBalance.getAvailableDebitAmount()+amount,
                    lastBalance.getPendingDebitAmount()-amount,
                    lastBalance.getPendingCreditCount(),
                    lastBalance.getPendingDebitCount()-1,
                    lastBalance.getAvailableCreditCount(),
                    lastBalance.getAvailableDebitCount()+1
            );


        }
        balance.setNumberAccount(numberAccount);
        return balance;
    }

    private Balance setPendingBalance(Balance lastBalance, Float amount, Boolean debitOrCredit,String numberAccount){

        Balance balance ;
        if(debitOrCredit) {
            balance= new Balance(
                    lastBalance.getAvailableCreditAmount(),
                    lastBalance.getPendingCreditAmount()+amount,
                    lastBalance.getAvailableDebitAmount(),
                    lastBalance.getPendingDebitAmount(),
                    lastBalance.getPendingCreditCount()+1,
                    lastBalance.getPendingDebitCount(),
                    lastBalance.getAvailableCreditCount(),
                    lastBalance.getAvailableDebitCount()
            );
        }else{
            balance = new Balance(
                    lastBalance.getAvailableCreditAmount(),
                    lastBalance.getPendingCreditAmount(),
                    lastBalance.getAvailableDebitAmount(),
                    lastBalance.getPendingDebitAmount()+amount,
                    lastBalance.getPendingCreditCount(),
                    lastBalance.getPendingDebitCount()+1,
                    lastBalance.getAvailableCreditCount(),
                    lastBalance.getAvailableDebitCount()


            );
        }
        balance.setNumberAccount(numberAccount);
        return balance;
    }


    private PaymentHistory setPaymentHistory(PaymentDTO payment, PayHistoryStatus payHistoryStatus, PaymentStatus paymentStatus,String systemReference){
        return new PaymentHistory(
                payment.getNumberAccountA(),
                payment.getNumberAccountB(),
                payHistoryStatus,
                paymentStatus,
                payment.getAmount(),
                payment.getExecutor(),
                payment.getCurrency(),
                payment.getUserReference(),
                systemReference,
                payment.getType()
        );
    }




    @Override
    @Transactional
    public PaymentHistory verifyPayment(PaymentDTO payment) {

        this.checkPayment(payment,false);
        List<PaymentHistory> paymentHistories = paymentHistoryRepository.
                    findPaymentHistoriesBySystemReferenceOrderByTimestampDesc(payment.getSystemReference());
        if(Objects.equals(paymentHistories.get(0).getAmount(), payment.getAmount())) {
            PaymentHistory paymentHistory = null;
//            if (payment.getAmount()*this.chooseRate(accountRepository.findAccountByNumberAccount(payment.getNumberAccountA()).getCurrency(), payment.getCurrency()) > 1000)
//                paymentHistory = this.setPaymentHistory(payment,
//                        PayHistoryStatus.VERIFIED, PaymentStatus.APPROVE, payment.getSystemReference());
//            else {
//                if (payment.getAmount()*this.chooseRate(accountRepository.findAccountByNumberAccount(payment.getNumberAccountA()).getCurrency(), payment.getCurrency()) > this.getLastBalancePayment(payment.getNumberAccountA()).getAvailableBalance())
//                    paymentHistory = this.setPaymentHistory(payment,
//                            PayHistoryStatus.VERIFIED, PaymentStatus.AUTHORIZE,payment.getSystemReference());
//
//                else return this.completePayment(payment);
            }
            return paymentHistoryRepository.save(null);
        }
//        else throw new ResourceNotFoundException("Payment","executor",payment.getExecutor(), HttpStatus.NOT_ACCEPTABLE);



    @Override
    @Transactional
    public PaymentHistory authorizePayment(PaymentDTO payment) {
        this.checkPayment(payment,true);
        return this.completePayment(payment);

    }


    @Override
    @Transactional
    public PaymentHistory approvePayment(PaymentDTO payment) {
        this.checkPayment(payment,true);
        PaymentHistory paymentHistory;
//        if (payment.getAmount()*this.chooseRate(accountRepository.findAccountByNumberAccount(payment.getNumberAccountA()).getCurrency(), payment.getCurrency()) > this.getLastBalancePayment(payment.getNumberAccountA()).getAvailableBalance())
//            paymentHistory = this.setPaymentHistory(payment,
//                    PayHistoryStatus.APPROVED, PaymentStatus.AUTHORIZE,payment.getSystemReference());
//        else return this.completePayment(payment);

        return paymentHistoryRepository.save(null);
    }




    @Override
    @Transactional
    public PaymentHistory insert(PaymentDTO payment) {
        checkInsertedPayment(payment);
//        balanceRepository.save(this.setPendingBalance(this.getLastBalancePayment
//                (payment.getNumberAccountA()), payment.getAmount()*this.chooseRate(accountRepository.findAccountByNumberAccount(payment.getNumberAccountA()).getCurrency(), payment.getCurrency()), false, payment.getNumberAccountA())
//        );
//        balanceRepository.save(this.setPendingBalance(this.getLastBalancePayment
//                (payment.getNumberAccountB()), payment.getAmount()*this.chooseRate(accountRepository.findAccountByNumberAccount(payment.getNumberAccountB()).getCurrency(), payment.getCurrency()), true, payment.getNumberAccountB())
//        );
//        System.out.println(this.chooseRate(accountRepository.findAccountByNumberAccount(payment.getNumberAccountA()).getCurrency(), payment.getCurrency()));
//        System.out.println(this.chooseRate(accountRepository.findAccountByNumberAccount(payment.getNumberAccountB()).getCurrency(), payment.getCurrency()));
        return paymentHistoryRepository.save(this.setPaymentHistory(payment, PayHistoryStatus.ENTERED, PaymentStatus.VERIFY,this.generateSystemReference()));

    }

    @Override
    @Transactional
    public List<PaymentHistoryDTO> mapHistory() {
        Set<PaymentHistory> historySet= new TreeSet<>(paymentHistoryRepository.findAll());
        List<String> systemReferences= historySet.stream().map(PaymentHistory::getSystemReference).toList();
        List<PaymentHistory> paymentHistories = new ArrayList<>();
        for(String s : systemReferences){
            List<PaymentHistory> paymentHistoryList = paymentHistoryRepository.
                    findPaymentHistoriesBySystemReferenceOrderByTimestampDesc(s);
            if(!paymentHistoryList.get(0).getAction().equals(PayHistoryStatus.COMPLETED)) {
                paymentHistories.add(paymentHistoryList.get(0));
            }
        }
        return paymentHistories.stream().map(payHistory ->new PaymentHistoryDTO(
                payHistory.getNumberAccountA(),
                payHistory.getNumberAccountB(),
                payHistory.getAmount(),
                payHistory.getExecutor(),
                payHistory.getCurrency(),
                payHistory.getUserReference(),
                payHistory.getSystemReference(),
                payHistory.getType(),
                payHistory.getStatus(),
                payHistory.getAction()
        )  ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaymentHistory cancelPayment(PaymentDTO paymentHistory) {
            Balance lastBalanceA = this.getLastBalancePayment(paymentHistory.getNumberAccountA());
            Balance lastBalanceB = this.getLastBalancePayment(paymentHistory.getNumberAccountB());
//            Balance debitor =  new Balance(lastBalanceA.getAvailableCreditAmount(), lastBalanceA.getPendingCreditAmount(),
//                    lastBalanceA.getAvailableDebitAmount(),
//                    lastBalanceA.getPendingDebitAmount() - paymentHistory.getAmount()*this.chooseRate(accountRepository.findAccountByNumberAccount(paymentHistory.getNumberAccountA()).getCurrency(), paymentHistory.getCurrency()),
//                    lastBalanceA.getPendingCreditCount(),
//                    lastBalanceA.getPendingDebitCount() - 1,
//                    lastBalanceA.getAvailableCreditCount(), lastBalanceA.getAvailableDebitCount()
//            );
//            debitor.setNumberAccount(paymentHistory.getNumberAccountA());
//            balanceRepository.save(debitor);
//            if(paymentHistory.getType().equals("Internal")) {
//                Balance creditor = new Balance(lastBalanceB.getAvailableCreditAmount(),
//                        lastBalanceB.getPendingCreditAmount() - paymentHistory.getAmount()*this.chooseRate(accountRepository.findAccountByNumberAccount(paymentHistory.getNumberAccountB()).getCurrency(), paymentHistory.getCurrency()),
//                        lastBalanceB.getAvailableDebitAmount(), lastBalanceB.getPendingDebitAmount(),
//                        lastBalanceB.getPendingCreditCount() - 1,
//                        lastBalanceB.getPendingDebitCount(), lastBalanceB.getAvailableCreditCount(),
//                        lastBalanceB.getAvailableDebitCount()
//                );
//                creditor.setNumberAccount(paymentHistory.getNumberAccountB());
//                balanceRepository.save(creditor);
//            }

            return paymentHistoryRepository.save(this.setPaymentHistory(paymentHistory, PayHistoryStatus.CANCELLED, null,paymentHistory.getSystemReference()));

    }


    @Override
    @Transactional
    public List<String> getBICs() throws ParserConfigurationException, IOException, SAXException {

        List<String> bics = new ArrayList<>();

        File file = new File(
                "/Users/iuliuandreisteau/Desktop/INTERNSHIP/PaymentSystem/BICreceiver.xml");
        DocumentBuilderFactory dbf
                = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);


        doc.getDocumentElement().normalize();
        System.out.println(
                "Root element: "
                        + doc.getDocumentElement().getNodeName());
        NodeList nodeList
                = doc.getElementsByTagName("bic");
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);
            System.out.println("\nNode Name :"
                    + node.getNodeName());
            if (node.getNodeType()
                    == Node.ELEMENT_NODE) {
                Element tElement = (Element) node;
                System.out.println(
                        "User id: "
                                + tElement
                                .getElementsByTagName("code")
                                .item(0)
                                .getTextContent());

                bics.add(tElement
                        .getElementsByTagName("code")
                        .item(0)
                        .getTextContent());
            }
        }
        return bics;
    }






    @Override
    @Transactional
    public void doExternalPayment(ExternalPaymentDTO externalPaymentDTO) throws IOException {

        new ModifyXmlDomParser(externalPaymentDTO);

        BufferedReader br = new BufferedReader(new FileReader("/Users/iuliuandreisteau/Desktop/INTERNSHIP/PaymentSystem/SENDER.xml"));
        String line;
        StringBuilder sb = new StringBuilder();

        while((line=br.readLine())!= null){
            sb.append(line.trim());
        }
        MediaType mediaType = MediaType.parse("text/xml");
        RequestBody body = RequestBody.create(mediaType, String.valueOf(sb));
        Request request = new Request.Builder()
                .url("https://ipsdemo.montran.ro/rtp/Message")
                .post(body)
                .addHeader("ceva","ceva")
                .addHeader("ceva","ceva")
                .addHeader("content-type", "text/xml")
                .build();

        String checker ="";
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            checker += Objects.requireNonNull(response.body()).string();
        }
        if(checker.contains("<GrpSts>ACCP</GrpSts>")) {
            Balance lastBalance = this.getLastBalancePayment(externalPaymentDTO.getSenderIBAN());
//            Balance balance =new Balance(
//                    lastBalance.getAvailableCreditAmount(),
//                    lastBalance.getPendingCreditAmount(),
//                    lastBalance.getAvailableDebitAmount(),
//                    lastBalance.getPendingDebitAmount()+externalPaymentDTO.getAmount()*this.chooseRate(accountRepository.findAccountByNumberAccount(externalPaymentDTO.getSenderIBAN()).getCurrency(),externalPaymentDTO.getCurrency()),
//                    lastBalance.getPendingCreditCount(),
//                    lastBalance.getPendingDebitCount()+1,
//                    lastBalance.getAvailableCreditCount(),
//                    lastBalance.getAvailableDebitCount()
//
//
//            );
//            System.out.println(this.chooseRate(accountRepository.findAccountByNumberAccount(externalPaymentDTO.getSenderIBAN()).getCurrency(),externalPaymentDTO.getCurrency()));
//            balance.setNumberAccount(externalPaymentDTO.getSenderIBAN());
//            balanceRepository.save(balance);

            System.out.println(new PaymentHistory(
                    externalPaymentDTO.getSenderIBAN(),
                    externalPaymentDTO.getReceiverIBAN(),
                    PayHistoryStatus.ENTERED,
                    PaymentStatus.VERIFY,
                    externalPaymentDTO.getAmount(),
                    externalPaymentDTO.getExecutor(),
                    externalPaymentDTO.getCurrency(),
                    externalPaymentDTO.getUserReference(),
                    externalPaymentDTO.getSystemReference(),
                    externalPaymentDTO.getType()));

            paymentHistoryRepository.save(new PaymentHistory(
                    externalPaymentDTO.getSenderIBAN(),
                    externalPaymentDTO.getReceiverIBAN(),
                    PayHistoryStatus.ENTERED,
                    PaymentStatus.VERIFY,
                    externalPaymentDTO.getAmount(),
                    externalPaymentDTO.getExecutor(),
                    externalPaymentDTO.getCurrency(),
                    externalPaymentDTO.getUserReference(),
                    externalPaymentDTO.getSystemReference(),
                    externalPaymentDTO.getType())
            );

        }

    }

    @Override
    @Transactional
    public List<PaymentDTO> findAll() {
        return paymentRepository.findAll().stream().map(payment -> new PaymentDTO(
                payment.getSender(),
                payment.getReceiver(),
                payment.getAmount(),
                null,
                payment.getCurrency(),
                payment.getUserReference(),
                payment.getSystemReference(),
                payment.getType())

        ).collect(Collectors.toList());
    }


}
