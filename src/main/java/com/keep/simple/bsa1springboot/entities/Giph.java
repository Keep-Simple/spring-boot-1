package com.keep.simple.bsa1springboot.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;

@NoArgsConstructor
@Data
public class Giph {
    private String id;
    private Path path;
}
