package com.example.demo.Repositorys;


import com.example.demo.Models.FileDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface FileDocumentRepository extends ElasticsearchRepository<FileDocument, String> {
}