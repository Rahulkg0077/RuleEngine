package com.hsbc.ruleengine.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Getter
@Setter
@Document(collection = "Payment")
public class Payment {

    private String paymentId;
    private String senderName;
    private String receiverName;
    private String Currency;
    private String creditType;
    private Long amount;
    private boolean status;
    private String additionalInfo;
    private final List<String> validationErrors = new ArrayList<>();

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    public void addValidationError(String error) {
        this.validationErrors.add(error);
    }
}
