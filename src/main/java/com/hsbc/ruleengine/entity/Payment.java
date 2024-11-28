package com.hsbc.ruleengine.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "Payment")
public class Payment {

    private String id;
    private String transactionId;
    private String senderName;
    private String receiveName;
    private String Currency;
    private String creditType;
    private Double amount;
    private String status;
    private LocalDate transactionDate;
    private String additionalInfo;
}
