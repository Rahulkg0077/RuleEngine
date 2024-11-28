package com.hsbc.ruleengine.entity;

import lombok.Data;
import lombok.Getter;

import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.nio.charset.StandardCharsets;

@Data
@Getter
@Setter
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

