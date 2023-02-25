package com.example.paymentsystem.controller;

import com.example.paymentsystem.dto.ExternalPaymentDTO;
import com.example.paymentsystem.dto.PaymentDTO;
import com.example.paymentsystem.dto.PaymentHistoryDTO;
import com.example.paymentsystem.entity.Payment;
import com.example.paymentsystem.history.PaymentHistory;
import com.example.paymentsystem.service.sketch.PaymentService;
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
public class PaymentController {

    @Autowired
    private final PaymentService paymentService ;


    @PostMapping(value="/payments/insert")
    public PaymentHistory insert(@RequestBody PaymentDTO paymentDTO){
        return paymentService.insert(paymentDTO);
    }

    @PostMapping(value="/payments/mapHistory")
    public List<PaymentHistoryDTO> mapHistory(){
        return paymentService.mapHistory();
    }

    @PostMapping(value="/payments/authorize")
    public PaymentHistory authorizePayment(@RequestBody PaymentDTO paymentDTO){
        return paymentService.authorizePayment(paymentDTO);
    }

    @PostMapping(value="/payments/verify")
    public PaymentHistory verifyPayment(@RequestBody PaymentDTO paymentDTO){
        return paymentService.verifyPayment(paymentDTO);
    }

    @PostMapping(value="/payments/cancel")
    public PaymentHistory cancelPayment(@RequestBody PaymentDTO paymentDTO){
        return paymentService.cancelPayment(paymentDTO);
    }


    @PostMapping(value="/payments/approve")
    public PaymentHistory approvePayment(@RequestBody PaymentDTO paymentDTO){
        return paymentService.approvePayment(paymentDTO);
    }

    @PostMapping("/payments/bic")
    public List<String> getCurrencies() throws ParserConfigurationException, IOException, SAXException {return  paymentService.getBICs();}

    @GetMapping("/payments")
    public List<PaymentDTO> findAll()  {
       return paymentService.findAll();
    }


    @PostMapping("/payments/external")
    public void doExternalPayment(@RequestBody ExternalPaymentDTO paymentDTO) throws  IOException {
        paymentService.doExternalPayment(paymentDTO);
    }



}
