package com.example.demo.Controller.FileGenerator;


import com.example.demo.ApiReponse.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


import com.example.demo.Services.FileGenerator.FileGeneratorService;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/file-gen")
public class FileGeneratorController {

    private final FileGeneratorService fileGeneratorService;

    public FileGeneratorController(FileGeneratorService fileGeneratorService) {
        this.fileGeneratorService = fileGeneratorService;
    }

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<String>> generateRandomFile(
            @RequestParam String fileName,
            @RequestParam(defaultValue = "5000") int words
    ) {
        try {
            String filePath = fileGeneratorService.generateRandomFile(fileName, words);
            ApiResponse<String> response = new ApiResponse<>(
                    HttpStatus.OK,
                    "The file was created successfully.",
                    filePath
            );
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            ApiResponse<String> response = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while creating the file: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

