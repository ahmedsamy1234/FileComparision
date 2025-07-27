package com.example.demo.Services.FileStorage;

import com.example.demo.ApiReponse.ApiResponse;
import com.example.demo.Services.Interfaces.FileIndexingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Service("redisIndexingStrategy")
@RequiredArgsConstructor
public class RedisFileIndexingStrategy implements FileIndexingStrategy {
    private final Jedis jedis;
    @Value("${files.directory}") private String filesDirectory;

    @Override
    public ApiResponse<List<String>> indexAllFiles() throws IOException {
        Path dir = Paths.get(filesDirectory);
        List<Path> files = Files.list(dir).filter(Files::isRegularFile).toList();
        List<String> indexedFiles = new ArrayList<>();
        for (Path file : files) {
            indexSingleFile(file.getFileName().toString());
            indexedFiles.add(file.getFileName().toString());
        }
        return new ApiResponse<>(
                HttpStatus.OK,
                "All files indexed in Redis successfully.",
                indexedFiles
        );
    }

    public ApiResponse<String> indexSingleFile(String fileName) throws IOException {
        Path filePath = Paths.get(filesDirectory, fileName);
        String redisKey = "file_words:" + fileName;
        final int CHUNK_SIZE = 1_000_000;
        char[] buffer = new char[CHUNK_SIZE];

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Files.newInputStream(filePath), StandardCharsets.UTF_8))) {

            int read;
            while ((read = reader.read(buffer, 0, CHUNK_SIZE)) != -1) {
                String chunk = new String(buffer, 0, read).toLowerCase();
                for (String word : chunk.split("\\W+")) {
                    if (!word.isBlank()) {
                        jedis.sadd(redisKey, word);
                    }
                }
            }
        }
        return new ApiResponse<>(
                HttpStatus.OK,
                "File indexed in Redis successfully (chunked by million chars).",
                fileName
        );
    }


    @Override
    public String getType() { return "redis"; }
}
