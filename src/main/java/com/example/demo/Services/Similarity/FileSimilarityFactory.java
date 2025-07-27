package com.example.demo.Services.Similarity;

import com.example.demo.Services.Interfaces.FileSimilarityStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class FileSimilarityFactory {

    private final List<FileSimilarityStrategy> strategies;


    public FileSimilarityStrategy getStrategy(String type) {
        return strategies.stream()
                .filter(s -> s.getType().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown similarity type: " + type));
    }
}