package com.keep.simple.bsa1springboot.helpers;

import com.keep.simple.bsa1springboot.dto.DirsDTO;
import com.keep.simple.bsa1springboot.dto.DirsResponseDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Helper {

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
        return cacheDtoToResponse(result);
    }

    public static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    public static boolean validate(String value) {
        try {
            Paths.get(value);
            return true;
        } catch (InvalidPathException e) {
            return false;
        }
    }
}
