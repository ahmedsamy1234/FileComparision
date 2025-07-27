package com.example.demo.Controller.Similarity;


import com.example.demo.ApiReponse.ApiResponse;
import com.example.demo.Dto.FileSimilarityResult;
import com.example.demo.Services.Interfaces.FileSimilarityStrategy;
import com.example.demo.Services.Similarity.FileSimilarityFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequiredArgsConstructor
@RequestMapping("/similarity")
public class FileSimilarityController {

    private final FileSimilarityFactory similarityFactory;

    @GetMapping("/similarity")
    public ResponseEntity<ApiResponse<FileSimilarityResult>> compareFiles(
            @RequestParam String file1,
            @RequestParam String file2,
            @RequestParam(defaultValue = "redis") String engine
    ) {
        FileSimilarityStrategy strategy = similarityFactory.getStrategy(engine);

        double similarity = strategy.calculateSimilarity(file1, file2);

        FileSimilarityResult result = new FileSimilarityResult(
                file1, file2, strategy.getType(), similarity
        );

        ApiResponse<FileSimilarityResult> response = new ApiResponse<>(
                HttpStatus.OK,
                "Similarity calculated successfully",
                result
        );

        return ResponseEntity.ok(response);
    }
}

