package com.example.paymentsystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
public class ExternalPaymentDTO {
    private String idMessage;
    private String dateTime;
    private String payMessage;
    private Float amount;
    private String date;
    private String payId;
    private String senderName;
    private String senderIBAN;
    private String BICReceiver;
    private String receiverIBAN;
    private String type;
    private String currency;
    private String executor;

    private String userReference;

    private String systemReference;

    public ExternalPaymentDTO(Float amount, String senderName, String senderIBAN, String BICReceiver, String receiverIBAN,String type,String currency,String executor,String userReference){
        String idMessage = generate7Digits();
        this.idMessage="MREF"+idMessage.substring(0,3)+"g"+idMessage.substring(3,6)+"fad"+idMessage.charAt(6);
        this.dateTime= String.valueOf(LocalDateTime.now().withNano(0));
        this.amount=amount;
        this.date= new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
        this.payId="PREF"+idMessage.substring(0,3)+"g"+idMessage.substring(3,6)+"fad"+idMessage.charAt(6);
        this.senderName=senderName;
        this.senderIBAN = senderIBAN;
        this.BICReceiver= BICReceiver;
        this.receiverIBAN= receiverIBAN;
        this.type=type;
        this.currency= currency;
        this.executor=executor;
        this.systemReference=this.generateSystemReference();
        this.userReference=userReference;


    }

    private String generateSystemReference(){
        return new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS", Locale.ENGLISH).format(new Date());
    }

    private String generate7Digits(){
        List<Integer> integerList= new ArrayList<>();
        for(int i=0;i<7;i++){
            integerList.add((int)Math.floor(Math.random()*10));
        }
        StringBuilder sevenDigits = new StringBuilder();
        for(Integer myInt : integerList){
            sevenDigits.append(myInt);
        }
        return String.valueOf(sevenDigits);
    }


}
