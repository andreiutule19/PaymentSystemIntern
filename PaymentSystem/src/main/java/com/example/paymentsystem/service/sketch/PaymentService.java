package com.example.paymentsystem.service.sketch;

import com.example.paymentsystem.dto.ExternalPaymentDTO;
import com.example.paymentsystem.dto.PaymentDTO;
import com.example.paymentsystem.dto.PaymentHistoryDTO;
import com.example.paymentsystem.history.PaymentHistory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;


public interface PaymentService {

    PaymentHistory verifyPayment(PaymentDTO payment);
    PaymentHistory authorizePayment(PaymentDTO payment);

    PaymentHistory insert(PaymentDTO paymentHistory);

    List<PaymentHistoryDTO> mapHistory();

    PaymentHistory cancelPayment(PaymentDTO paymentHistory);

    PaymentHistory approvePayment(PaymentDTO payment);

    List<String> getBICs() throws ParserConfigurationException, IOException, SAXException;

    void doExternalPayment(ExternalPaymentDTO externalPaymentDTO) throws IOException;

    List<PaymentDTO> findAll();

}
