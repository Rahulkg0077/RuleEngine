package com.hsbc.ruleengine.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Payment")
public class Payment {

    private String paymentId;
    private String senderName;
    private String receiveName;
    private String Currency;
    private String creditType;
    private Long amount;
    private boolean status;
    private String additionalInfo;
}
