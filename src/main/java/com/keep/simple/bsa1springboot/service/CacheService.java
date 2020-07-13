package com.keep.simple.bsa1springboot.service;

import com.keep.simple.bsa1springboot.dto.DirsDTO;
import com.keep.simple.bsa1springboot.dto.DirsResponseDTO;
import com.keep.simple.bsa1springboot.dto.GiphResponseDto;
import com.keep.simple.bsa1springboot.helpers.Helper;
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

import static com.keep.simple.bsa1springboot.helpers.Helper.getDirsResponseDTOS;


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
    public List<DirsResponseDTO> getAll() {
        Path start = Paths.get(startPath);
        return getDirsResponseDTOS(start);
    }

    @SneakyThrows
    public List<DirsResponseDTO> getGiphsByQuery(String query) {
        var result = new DirsDTO();

        Path concreteStart = Paths.get(startPath + "\\" + query);
        result.addDir(concreteStart);

        if (Files.exists(concreteStart)) {
            Files.walk(concreteStart)
                    .filter(Files::isRegularFile)
                    .forEach(result::addGiph);
        }

        return Helper.cacheDtoToResponse(result);
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
