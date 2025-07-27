package com.example.demo.Services.Similarity;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.example.demo.Models.FileDocument;
import com.example.demo.Services.Interfaces.FileSimilarityStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service("esSimilarityStrategy")
@RequiredArgsConstructor
public class EsSimilarityStrategy implements FileSimilarityStrategy {
    private final ElasticsearchClient esClient;

    @Override
    public double calculateSimilarity(String file1, String file2) {

        try {
            String contentA = getFileContent(file1);
            String contentB = getFileContent(file2);
            Set<String> setA = new HashSet<>(Arrays.asList(contentA.toLowerCase().split("\\W+")));
            Set<String> setB = new HashSet<>(Arrays.asList(contentB.toLowerCase().split("\\W+")));
            setA.removeIf(String::isBlank); setB.removeIf(String::isBlank);
            Set<String> intersection = new HashSet<>(setA); intersection.retainAll(setB);
            Set<String> union = new HashSet<>(setA); union.addAll(setB);
            return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size() * 100;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public String getType() {
        return "elasticsearch";
    }
    // Utility to get file content by id
    private String getFileContent(String fileId) throws IOException {
        var response = esClient.get(g -> g
                .index("files_index")
                .id(fileId), FileDocument.class);
        if (!response.found())
            throw new RuntimeException("File not found: " + fileId);
        return response.source().content();
    }

    //    public List<FileSimilarityResult> findMostSimilarFiles(String fileName, int topN) throws IOException {
//        String content = getFileContent(fileName);
//
//        Query query = Query.of(q -> q.moreLikeThis(
//                MoreLikeThisQuery.of(m -> m
//                        .fields("content")
//                        .like(l -> l.text(content))
//                        .minTermFreq(1)
//                        .minDocFreq(1)
//                )
//        ));
//
//        SearchRequest searchRequest = SearchRequest.of(s -> s
//                .index("files_index")
//                .query(query)
//                .size(topN + 1) // +1 to skip the file itself
//        );
//
//        SearchResponse<FileDocument> response = esClient.search(searchRequest, FileDocument.class);
//
//        return response.hits().hits().stream()
//                .limit(topN)
//                .map(hit -> new FileSimilarityResult(
//                        hit.id(),
//                        hit.score() != null ? hit.score() : 0f
//                ))
//                .toList();
//    }
//



}
