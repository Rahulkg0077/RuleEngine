package com.hsbc.ruleengine.repository;

import com.hsbc.ruleengine.entity.FileModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends MongoRepository<FileModel, String> {

    List<FileModel> findByFileType(String fileType);
    FileModel findByFileName(String name);
}
