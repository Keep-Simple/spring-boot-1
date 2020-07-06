package com.keep.simple.bsa1springboot.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.util.UUID;

@NoArgsConstructor
@Data
public class Giph {
    private UUID id;
    private Path path;
}
