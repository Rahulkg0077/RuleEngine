package com.hsbc.ruleengine.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hsbc.ruleengine.entity.FileModel;
import com.hsbc.ruleengine.entity.Payment;
import com.hsbc.ruleengine.repository.FileRepository;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    PaymentService paymentService;

    public FileModel storeFile(MultipartFile file) throws IOException {
        FileModel model = new FileModel();
        String str = returnFileType(file.getOriginalFilename());
        model.setFileType(str);
        model.setFileName(file.getOriginalFilename());
        model.setData(file.getBytes());

        return fileRepository.save(model);
    }

    private String returnFileType(String fileType) {
        int lastIndex = fileType.lastIndexOf(".");
        return fileType.substring(lastIndex + 1);
    }

    public List<FileModel> getAllFiles() {
        return fileRepository.findAll();
    }

    public List<FileModel> getFilesByType(String fileType) {
        return fileRepository.findByFileType(fileType);
    }

    public List<Payment> processFiles() {
        List<FileModel> files = fileRepository.findAll();
        List<Payment> payments = new ArrayList<Payment>();
        for (FileModel f : files) {
            if (f.getFileType().equals("txt")) {
                Payment payment = paymentService.processTXTFile(f);
                payments.add(payment);
            } else if (f.getFileType() == "pdf") {
                Payment payment = paymentService.processPDFFile(f);
                payments.add(payment);
            }

        }
        return payments;

    }

}
