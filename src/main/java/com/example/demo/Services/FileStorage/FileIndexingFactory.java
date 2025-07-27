package com.example.demo.Services.FileStorage;

import com.example.demo.Services.Interfaces.FileIndexingStrategy;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileIndexingFactory {
    private final List<FileIndexingStrategy> strategies;

    public FileIndexingStrategy getStrategy(String type) {
        return strategies.stream()
                .filter(s -> s.getType().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown indexing type: " + type));
    }
}
