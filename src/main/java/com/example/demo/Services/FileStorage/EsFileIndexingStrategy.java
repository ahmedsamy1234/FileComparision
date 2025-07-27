package com.example.demo.Services.FileStorage;

import com.example.demo.ApiReponse.ApiResponse;
import com.example.demo.Models.FileDocument;
import com.example.demo.Repositorys.FileDocumentRepository;
import com.example.demo.Services.Interfaces.FileIndexingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Service("esIndexingStrategy")
@RequiredArgsConstructor
public class EsFileIndexingStrategy implements FileIndexingStrategy {
    private final FileDocumentRepository repository;
    @Value("${files.directory}") private String directoryPath;

    @Override
    public ApiResponse<List<String>> indexAllFiles() throws IOException {
        Path dir = Paths.get(directoryPath);
        List<Path> files = Files.list(dir).filter(Files::isRegularFile).toList();
        List<String> indexedFiles = new ArrayList<>();
        for (Path file : files) {
            indexSingleFile(file.getFileName().toString());
            indexedFiles.add(file.getFileName().toString());
        }
        return new ApiResponse<>(
                HttpStatus.OK,
                "All files indexed in Elasticsearch successfully.",
                indexedFiles
        );
    }

    @Override
    public ApiResponse<String> indexSingleFile(String fileName) throws IOException {
        Path filePath = Paths.get(directoryPath, fileName);
        if (!Files.exists(filePath)) throw new FileNotFoundException("File not found: " + filePath);
        String content = Files.readString(filePath);
        repository.save(new FileDocument(fileName, content));
        return new ApiResponse<>(
                HttpStatus.OK,
                "File indexed in Elasticsearch successfully.",
                fileName
        );
    }

    @Override
    public String getType() { return "elasticsearch"; }
}
