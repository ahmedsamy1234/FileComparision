package com.example.demo.Services.Interfaces;

import com.example.demo.ApiReponse.ApiResponse;

import java.io.IOException;
import java.util.List;

public interface FileIndexingStrategy {
    ApiResponse<List<String>> indexAllFiles() throws IOException;
    ApiResponse<String> indexSingleFile(String fileName) throws IOException;
    String getType();
}