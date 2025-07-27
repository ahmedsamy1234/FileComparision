package com.example.demo.Controller.Filestorage;

import com.example.demo.ApiReponse.ApiResponse;
import com.example.demo.Services.FileStorage.FileIndexingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
    @RequestMapping("/Filestorage")
public class FilestorageController {
    private final FileIndexingService service;

    @PostMapping("/index")
    public ResponseEntity<ApiResponse<String>> indexFiles(
            @RequestParam String engine,
            @RequestParam String mode,
            @RequestParam(required = false) String fileName) {
        try {
            service.index(engine, mode, fileName);
            String msg = "Indexing " + ("all".equalsIgnoreCase(mode) ? "all files" : fileName)
                    + " with " + engine + " completed successfully.";
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, msg, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        }
    }
}
