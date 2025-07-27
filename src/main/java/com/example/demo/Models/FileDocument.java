package com.example.demo.Models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "files_index")
public record FileDocument(
        @Id String fileName,
        String content
) {}

