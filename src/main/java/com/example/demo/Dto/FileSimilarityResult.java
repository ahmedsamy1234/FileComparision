package com.example.demo.Dto;

public record FileSimilarityResult(String file1, String file2, String similarityType, Double similarityPercent) {
}