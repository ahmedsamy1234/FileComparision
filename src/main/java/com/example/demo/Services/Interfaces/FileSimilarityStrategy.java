package com.example.demo.Services.Interfaces;

public interface FileSimilarityStrategy {
    double calculateSimilarity(String file1, String file2);
    String getType(); // مثلاً: "redis" أو "elasticsearch"
}