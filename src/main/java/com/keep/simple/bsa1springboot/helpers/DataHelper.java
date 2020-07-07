package com.keep.simple.bsa1springboot.helpers;

import com.keep.simple.bsa1springboot.dto.DirsDTO;
import com.keep.simple.bsa1springboot.dto.DirsResponseDTO;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataHelper {

    public static ArrayList<DirsResponseDTO> cacheDtoToResponse(DirsDTO dto) {
        var result = new ArrayList<DirsResponseDTO>();

        for (Map.Entry<String, Set<Path>> entry : dto.getMap().entrySet()) {
            var entity = new DirsResponseDTO();

            entity.setQuery(entry.getKey());

            entity.setGifs(new ArrayList<>(entry.getValue()));

            result.add(entity);
        }

        return result;
    }

    public static List<DirsResponseDTO> getDirsResponseDTOS(DirsDTO result, Path start) throws IOException {
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
                if (!file.getFileName().toString().endsWith(".csv")) {
                    result.addGiph(file);
                }

                return FileVisitResult.CONTINUE;
            }
        });
        return DataHelper.cacheDtoToResponse(result);
    }
}
