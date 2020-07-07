package com.keep.simple.bsa1springboot.service;

import com.keep.simple.bsa1springboot.dto.GiphResponseDto;
import kong.unirest.Unirest;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class CacheService {
    @Value("${storage.cache.location}")
    private String location;

    @SneakyThrows
    public Optional<Path> getGiphByQuery(String query) {
        Path path = Paths.get(String.format(location, query));

        if (!Files.exists(path)) {
            return Optional.empty();
        }

        try (Stream<Path> paths = Files.walk(path)) {
            return paths.filter(Files::isRegularFile).findAny();
        }
    }

    @SneakyThrows
    public Optional<Path> save(GiphResponseDto giph, String query) {
        String queryDir = String.format(location, query);
        String gifDir = queryDir + giph.getId() + ".gif";

        if (Files.exists(Paths.get(gifDir))) {
            return Optional.empty();
        }

        Path path = Paths.get(queryDir);

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File result = Unirest.get(giph.getUrl()).asFile(gifDir).getBody();

        return Optional.of(result.toPath());
    }
}
