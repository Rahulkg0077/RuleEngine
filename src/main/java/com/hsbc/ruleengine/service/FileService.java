package com.hsbc.ruleengine.service;

import com.hsbc.ruleengine.entity.FileModel;
import com.hsbc.ruleengine.repository.FileRepository;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private KieSession kieSession;

    public FileModel storeFile(MultipartFile file) throws IOException {
        FileModel model = new FileModel();
        String str = returnFileType(file.getOriginalFilename());
        model.setFileType(str);
        model.setFileName(file.getOriginalFilename());
        model.setData(file.getBytes());

        return fileRepository.save(model);
    }

    private String returnFileType(String fileType){
        int lastIndex = fileType.lastIndexOf(".");
        return fileType.substring(lastIndex + 1);
    }

    public List<FileModel> getAllFiles(){
        return fileRepository.findAll();
    }

    public List<FileModel> getFilesByType(String fileType){
        return fileRepository.findByFileType(fileType);
    }

    public void processFiles(){
        List<FileModel> files = fileRepository.findAll();

        try {
            for(FileModel f : files){
                kieSession.insert(f);
                kieSession.fireAllRules();
            }
        } finally {
            kieSession.dispose();
        }
    }

}
