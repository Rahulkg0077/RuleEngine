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
            System.out.println("The PDF file is : " + text);

        } catch (IOException e) {
            throw new FileProcessingException("Error processing PDF file", e);
        }
    }

    public void processTXTFile(FileModel fileModel){
        System.out.println("The TXT file is : " + fileModel.getFileContentAsString());
    }
}
