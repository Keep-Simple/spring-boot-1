package com.keep.simple.bsa1springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DirsDTO {
    private HashMap<String, Set<Path>> map = new HashMap<>();

    @SneakyThrows
    public boolean addGiph(Path giphDir) {
        return map.get(giphDir.getParent().getFileName().toString()).add(giphDir);
    }

    public void addDir(Path dir) {
        map.put(dir.getFileName().toString(), new HashSet<>());
    }
}
