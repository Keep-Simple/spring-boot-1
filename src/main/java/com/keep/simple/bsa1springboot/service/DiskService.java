package com.keep.simple.bsa1springboot.service;

import com.keep.simple.bsa1springboot.dto.DirsDTO;
import com.keep.simple.bsa1springboot.dto.DirsResponseDTO;
import com.keep.simple.bsa1springboot.dto.UserHistoryDTO;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static com.keep.simple.bsa1springboot.helpers.Helper.deleteDirectory;
import static com.keep.simple.bsa1springboot.helpers.Helper.getDirsResponseDTOS;


@Service
public class DiskService {

    @Value("${storage.main.location}")
    private String location;

    @Value("${storage.main.location.global}")
    private String startLocation;

    @SneakyThrows
    public Path save(Path giphPath, String username, String query) {
        String save = String.format(location, username) + query;

        Path savePath = Paths.get(save);

        if (!Files.exists(savePath)) {
            Files.createDirectories(savePath);
        }

        savePath = Paths.get(save + "\\" + giphPath.getFileName());

        writeLogToFile(username, savePath, query);

        return Files.copy(giphPath, savePath,
                StandardCopyOption.REPLACE_EXISTING).toAbsolutePath();
    }

    @SneakyThrows
    public Optional<Path> getGiph(String query, String name) {
        String str = String.format(location, name) + query;

        Path searchPath = Paths.get(str);

        if (!Files.exists(searchPath)) {
            return Optional.empty();
        }

        try (Stream<Path> paths = Files.walk(searchPath)) {
            return paths.filter(Files::isRegularFile).findAny();
        }

    }

    @SneakyThrows
    public Set<Path> getAll() {
        var result = new HashSet<Path>();
        Path root = Paths.get(startLocation);

        Files.walkFileTree(root, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (!file.getFileName().toString().endsWith(".csv")) {
                    result.add(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });

        return result;
    }

    @SneakyThrows
    public List<DirsResponseDTO> getAllForUser(String name) {
        String str = String.format(location, name);

        Path start = Paths.get(str);

        if (!Files.exists(start)) {
            return new ArrayList<>();
        }

        return getDirsResponseDTOS(start);
    }

    @SneakyThrows
    public void clearUser(String username) {
        String str = String.format(location, username);
        Path deletePath = Paths.get(str);

        if (Files.exists(deletePath)) {
            deleteDirectory(deletePath.toFile());
        }
    }

    /**
     * History.cvs
     */

    @SneakyThrows
    public void writeLogToFile(String username, Path savePath, String query) {

        FileWriter out = new FileWriter(String.format(location, username) + "\\history.csv", true);
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT)) {
            printer.printRecord(
                    LocalDate.now(),
                    query,
                    savePath.toAbsolutePath().toUri());
        }
    }

    @SneakyThrows
    public ArrayList<UserHistoryDTO> readLog(String username) {
        var result = new ArrayList<UserHistoryDTO>();
        String str = String.format(location, username) + "\\history.csv";

        if (!Files.exists(Paths.get(str))) {
            return new ArrayList<>();
        }

        var in = new FileReader(str);

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
        for (CSVRecord record : records) {
            result.add(new UserHistoryDTO(
                    record.get(0),
                    record.get(1),
                    record.get(2)
            ));
        }
        return result;
    }

    @SneakyThrows
    public void clearLog(String username) {
        String str = String.format(location, username) + "\\history.csv";
        Path path = Paths.get(str);

        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

}
