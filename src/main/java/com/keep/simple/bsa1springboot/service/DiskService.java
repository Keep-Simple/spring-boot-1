package com.keep.simple.bsa1springboot.service;

import com.keep.simple.bsa1springboot.entities.Giph;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;


@Service
public class DiskService {

    @Value("${storage.main.location}")
    private String location;

    public Giph generateGifForUser(String query, String username) {
        return null;
    }

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
    public void writeLogToFile(String username, Path savePath, String query) {

        FileWriter out = new FileWriter(String.format(location, username) + "\\history.csv", true);
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT)) {
                printer.printRecord(LocalDate.now(), query, savePath);
        }
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
}
