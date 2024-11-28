package com.hsbc.ruleengine.service;

import com.hsbc.ruleengine.Exceptions.FileProcessingException;
import com.hsbc.ruleengine.entity.FileModel;
import com.hsbc.ruleengine.entity.Payment;
import com.hsbc.ruleengine.repository.FileRepository;
import com.hsbc.ruleengine.repository.PaymentRepository;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private FileRepository fileRepository;

    public List<Payment> getPendingPayments(){
        return paymentRepository.findByStatus("Pending");
    }

    public List<Payment> getCompletedPayments(){
        return paymentRepository.findByStatus("Complete ");
    }

    public void processPDFFile(FileModel fileModel){
        try(PDDocument doc = Loader.loadPDF(fileModel.getData())){
            String text = new PDFTextStripper().getText(doc);
//            System.out.println(text);
        } catch (IOException e) {
            throw new FileProcessingException("Error processing PDF file", e);
        }
    }

    public void processTXTFile(FileModel fileModel){
        String text  = fileModel.getFileContentAsString();
        var payment = new Payment();
        String[] lines = text.split("\n");

        payment.setTransactionId(lines[1].substring(lines[1].lastIndexOf(":") + 1));
        payment.setSenderName(lines[5]);
        payment.setReceiveName(lines[9]);
        payment.setStatus("Success");
        payment.setAdditionalInfo(lines[13].substring(lines[13].lastIndexOf(":") + 1));

        String trimmedInput = lines[3].substring(lines[3].lastIndexOf(":") + 1);
        payment.setTransactionDate(convertStringToDate(trimmedInput.substring(0, 6)));
        payment.setCurrency(trimmedInput.substring(6, 9));
        payment.setAmount(Double.parseDouble(trimmedInput.substring(9, trimmedInput.length() - 2)));

        paymentRepository.save(payment);
    }

    public void processCSVFile(FileModel fileModel){
        String text = fileModel.getFileContentAsString();
        String[] lines = text.split("\n");

        for (int i = 1; i < lines.length; i++){
            Payment payment = new Payment();
            String[] line = lines[i].split(",");
            payment.setTransactionId(line[0]);
            payment.setSenderName(line[4]);
            payment.setReceiveName(line[5]);
            payment.setAmount(Double.parseDouble(line[8]));
            payment.setStatus("Success");
            payment.setTransactionDate(convertStringToDate(line[1]));
            payment.setCreditType(line[9]);
            paymentRepository.save(payment);
        }
    }

    private LocalDate convertStringToDate(String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        try {
            return LocalDate.parse(dateString, formatter);
        }catch (DateTimeParseException e){
            System.out.println("Invalid Date format : " + dateString);
        }

        return null;
    }
}
