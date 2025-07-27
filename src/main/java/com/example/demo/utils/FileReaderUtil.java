package com.example.demo.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class FileReaderUtil {

    public static String readFileContent(Path path) throws IOException {
        return Files.lines(path)
                .map(String::trim)
                .collect(Collectors.joining(" "));
    }
}
