package com.keep.simple.bsa1springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DirsResponseDTO {
    private String query;
    private ArrayList<Path> gifs = new ArrayList<>();
}
