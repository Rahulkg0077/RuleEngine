package com.hsbc.ruleengine.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hsbc.ruleengine.entity.FileModel;
import com.hsbc.ruleengine.entity.Payment;
import com.hsbc.ruleengine.service.FileService;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/fire")
    public List<Payment> fireRules() {
        return fileService.processFiles();

    }

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            FileModel fileModel = fileService.storeFile(file);
            return ResponseEntity.ok().body("File Uploaded Successfully: " + fileModel.getFileName());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Could not upload the file: " + e.getMessage());
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<?> findFilesByType(@PathVariable String type) {
        List<FileModel> files = fileService.getFilesByType(type);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllFiles() {
        List<FileModel> files = fileService.getAllFiles();
        return ResponseEntity.ok(files);
    }
}
