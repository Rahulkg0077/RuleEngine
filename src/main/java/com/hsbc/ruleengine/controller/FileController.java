package com.hsbc.ruleengine.controller;

import com.hsbc.ruleengine.entity.FileModel;
import com.hsbc.ruleengine.service.FileService;
import com.hsbc.ruleengine.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private PaymentService paymentService;


    @GetMapping("/fire")
    public String fireRules(){
        fileService.processFiles();
        return "Success";
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file){
        try {
            FileModel fileModel = fileService.storeFile(file);
            return ResponseEntity.ok().body("File Uploaded Successfully: " + fileModel.getFileName());
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("Could not upload the file: " + e.getMessage());
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<?> findFilesByType(@PathVariable String type){
        List<FileModel> files = fileService.getFilesByType(type);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllFiles(){
        List<FileModel> files = fileService.getAllFiles();
        return ResponseEntity.ok(files);
    }
}
