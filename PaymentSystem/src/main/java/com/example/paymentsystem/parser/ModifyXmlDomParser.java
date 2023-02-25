package com.example.paymentsystem.parser;

import com.example.paymentsystem.dto.ExternalPaymentDTO;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class ModifyXmlDomParser {
    private static final String FILENAME = "/Users/iuliuandreisteau/Desktop/INTERNSHIP/PaymentSystem/exemplu_pacs_008.xml";

    public ModifyXmlDomParser(ExternalPaymentDTO ep){

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try (InputStream is = new FileInputStream(FILENAME)) {

            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(is);

            setNodeOfXML(doc,"BizMsgIdr",ep.getIdMessage(),0);
            setNodeOfXML(doc,"CreDt",ep.getDateTime()+"Z",0);
            setNodeOfXML(doc,"grphdr:MsgId",ep.getIdMessage(),0);
            setNodeOfXML(doc,"grphdr:CreDtTm",ep.getDateTime()+"+03:00",0);
            setNodeOfXML(doc,"grphdr:TtlIntrBkSttlmAmt", String.valueOf(ep.getAmount()),0);
            setNodeOfXML(doc,"grphdr:IntrBkSttlmDt", ep.getDate(), 0);
            setNodeOfXML(doc,"crdt:InstrId", ep.getPayId(), 0);
            setNodeOfXML(doc,"crdt:EndToEndId", ep.getPayId(),0);
            setNodeOfXML(doc,"crdt:TxId", ep.getPayId(),0);
            setNodeOfXML(doc,"crdt:IntrBkSttlmAmt",String.valueOf(ep.getAmount()),0);
            setNodeOfXML(doc,"crdt:AccptncDtTm", ep.getDateTime()+"+03:00",0);
            setNodeOfXML(doc,"crdt:Nm",ep.getSenderName(),0);
            setNodeOfXML(doc,"crdt:IBAN",ep.getSenderIBAN(),0);
            setNodeOfXML(doc,"crdt:BICFI",ep.getBICReceiver(),1);
            setNodeOfXML(doc,"crdt:AnyBIC", ep.getBICReceiver(), 1);
            setNodeOfXML(doc,"crdt:IBAN", ep.getReceiverIBAN(), 1);


            try (FileOutputStream output =
                         new FileOutputStream("/Users/iuliuandreisteau/Desktop/INTERNSHIP/PaymentSystem/SENDER.xml")) {
                writeXml(doc, output);
            }


        } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
            throw new RuntimeException(e);
        }


    }

    private void setNodeOfXML(Document doc, String tagName, String value,Integer k){
        NodeList listOfID = doc.getElementsByTagName(tagName);
        Node firstID = listOfID.item(k);
        firstID.setTextContent(value);
    }

    private void writeXml(Document doc,
                                 OutputStream output)
            throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);

    }
}




