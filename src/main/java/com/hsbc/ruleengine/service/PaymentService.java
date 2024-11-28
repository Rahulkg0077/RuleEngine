package com.hsbc.ruleengine.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hsbc.ruleengine.Exceptions.FileProcessingException;
import com.hsbc.ruleengine.entity.FileModel;
import com.hsbc.ruleengine.entity.Payment;
import com.hsbc.ruleengine.repository.PaymentRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private KieSession kieSession;

    public List<Payment> getPendingPayments() {
        return paymentRepository.findByStatus("Pending");
    }

    public List<Payment> getCompletedPayments() {
        return paymentRepository.findByStatus("Complete ");
    }

    public Payment processPDFFile(FileModel fileModel) {
        try (PDDocument doc = Loader.loadPDF(fileModel.getData())) {
            String text = new PDFTextStripper().getText(doc);
            System.out.println("The PDF file is : " + text);
            Payment payment = new Payment();
            return payment;

        } catch (IOException e) {
            throw new FileProcessingException("Error processing PDF file", e);
        }
    }

    public Payment processTXTFile(FileModel fileModel) {
        System.out.println("The TXT file is : " + fileModel.getFileContentAsString());
        Payment payment = parseSwiftFile(fileModel.getData());
        return payment;
    }

    public Payment parseSwiftFile(byte[] data) {
        Payment payment = new Payment();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)))) {
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            String[] lines = content.toString().split("\n");

            for (String ln : lines) {
                System.out.println(ln);

                if (ln.startsWith(":20:")) {
                    payment.setPaymentId(ln.substring(4).trim());
                } else if (ln.startsWith(":50K:")) {
                    payment.setSenderName(extractName(lines, ln));
                } else if (ln.startsWith(":59:")) {
                    payment.setReceiverName(extractName(lines, ln));
                } else if (ln.startsWith(":32A:")) {
                    payment.setCurrency(ln.substring(10, 13).trim());

                    String amountString = ln.substring(13).replace(",", "").replaceAll("[^0-9]", "").trim();
                    System.out.println("Extracted amount string: " + amountString);

                    try {
                        long amount = Long.parseLong(amountString);
                        System.out.println("Parsed amount: " + amount);
                        payment.setAmount(amount);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid amount format: " + amountString);
                        payment.setAmount(0L);
                    }
                } else if (ln.startsWith(":23B:")) {
                    payment.setCreditType(ln.substring(4).trim());
                } else if (ln.startsWith(":79:")) {
                    payment.setAdditionalInfo(ln.substring(4).trim());
                }
            }
            payment.setStatus(true);
            kieSession.insert(payment);
            kieSession.fireAllRules();
        } catch (IOException e) {
            e.printStackTrace();
            payment.setStatus(false);
        }
        return payment;
    }

    private static String extractName(String[] lines, String currentLine) {
        StringBuilder name = new StringBuilder();
        boolean startAppending = false;
        for (String line : lines) {
            if (line.equals(currentLine)) {
                startAppending = true;
                continue;
            }
            if (startAppending && !line.startsWith(":")) {
                name.append(line.trim()).append(" ");
            } else if (startAppending && line.startsWith(":")) {
                break;
            }
        }
        return name.toString().trim();
    }

    public void addValidationError(Payment payment, String error) {
        payment.addValidationError(error);
    }

}
