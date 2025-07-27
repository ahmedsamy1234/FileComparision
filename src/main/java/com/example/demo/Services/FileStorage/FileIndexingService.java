package com.example.demo.Services.FileStorage;

import com.example.demo.ApiReponse.ApiResponse;
import com.example.demo.Services.Interfaces.FileIndexingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileIndexingService {
    private final FileIndexingFactory factory;

    public ApiResponse<?> index(String engine, String mode, String fileName) throws IOException {
        FileIndexingStrategy strategy = factory.getStrategy(engine);
        if ("all".equalsIgnoreCase(mode)) {
            return strategy.indexAllFiles();
        } else if ("single".equalsIgnoreCase(mode)) {
            if (fileName == null || fileName.isBlank())
                throw new IllegalArgumentException("File name must be provided for single mode");
            return strategy.indexSingleFile(fileName);
        } else {
            throw new IllegalArgumentException("Invalid mode: " + mode);
        }
    }
}






