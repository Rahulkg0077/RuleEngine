package com.hsbc.ruleengine.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.nio.charset.StandardCharsets;

@Data
@Document(collection = "files")
public class FileModel {

    private String id;
    private String fileName;
    private String fileType;

    @Field("data")
    private byte[] data;

    public String getFileContentAsString(){
        return new String(data, StandardCharsets.UTF_8);
    }
}

