package com.keep.simple.bsa1springboot.service;

import com.keep.simple.bsa1springboot.dto.CacheDTO;
import com.keep.simple.bsa1springboot.dto.CacheResponseDTO;
import com.keep.simple.bsa1springboot.dto.GiphResponseDto;
import com.keep.simple.bsa1springboot.helpers.CacheMapper;
import kong.unirest.Unirest;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class CacheService {
    @Value("${storage.cache.location}")
    private String location;

    @Value("${storage.cache.location.global}")
    private String startPath;

    @SneakyThrows
    public Optional<Path> getOneByQuery(String query) {
        Path path = Paths.get(String.format(location, query));

        if (!Files.exists(path)) {
            return Optional.empty();
        }

        try (Stream<Path> paths = Files.walk(path)) {
            return paths.filter(Files::isRegularFile).findAny();
        }
    }

    @SneakyThrows
    public List<CacheResponseDTO> getAll() {
        var result = new CacheDTO();

        Path start = Paths.get(startPath);
        Files.walkFileTree(start, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                if (dir.equals(start)) {
                    return FileVisitResult.CONTINUE;
                }

                result.addDir(dir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                result.addGiph(file);
                return FileVisitResult.CONTINUE;
            }
        });
        return CacheMapper.cacheDtoToResponse(result);
    }

    @SneakyThrows
    public List<CacheResponseDTO> getGiphsByQuery(String query) {
        var result = new CacheDTO();

        Path concreteStart = Paths.get(startPath + "\\" + query);
        result.addDir(concreteStart);

        try {
            Files.walk(concreteStart)
                    .filter(Files::isRegularFile)
                    .forEach(result::addGiph);
        } finally {
            return CacheMapper.cacheDtoToResponse(result);
        }
    }

    @SneakyThrows
    public Optional<Path> save(GiphResponseDto giph, String query) {
        String queryDir = String.format(location, query);
        String gifDir = queryDir + giph.getId() + ".gif";

        if (Files.exists(Paths.get(gifDir))) {
            return Optional.of(Paths.get(gifDir));
        }

        Path path = Paths.get(queryDir);

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File result = Unirest.get(giph.getUrl()).asFile(gifDir).getBody();

        return Optional.of(result.toPath());
    }

    @SneakyThrows
    public void deleteAll() {
        Path start = Paths.get(startPath);
        Files.walkFileTree(start, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (!dir.equals(start))
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
