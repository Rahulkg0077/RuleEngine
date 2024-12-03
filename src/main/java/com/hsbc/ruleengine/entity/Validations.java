package com.hsbc.ruleengine.entity;

import java.util.regex.Pattern;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "validations")
public class Validations {
    @Id
    private String id;
    private String fieldName;
    private String type;
    private boolean required;
    private Integer maxLength;
    private String pattern;
    private String description;
    private Long minValue;

    public boolean validate(Object value) {
        if (value == null && required) {
            return false;
        }
        if (value instanceof String) {
            String strValue = (String) value;
            if (required && strValue.isEmpty()) {
                return false;
            }
            if (maxLength != null && strValue.length() > maxLength) {
                return false;
            }
            if (pattern != null && !Pattern.matches(pattern, strValue)) {
                return false;
            }
        } else if (value instanceof Long) {
            Long longValue = (Long) value;
            if (minValue != null && longValue <= minValue) {
                return false;
            }
        }
        return true;
    }
}
