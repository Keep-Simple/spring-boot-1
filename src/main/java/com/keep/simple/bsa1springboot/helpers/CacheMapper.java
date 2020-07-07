package com.keep.simple.bsa1springboot.helpers;

import com.keep.simple.bsa1springboot.dto.CacheDTO;
import com.keep.simple.bsa1springboot.dto.CacheResponseDTO;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class CacheMapper {

    public static ArrayList<CacheResponseDTO> cacheDtoToResponse(CacheDTO dto) {
        var result = new ArrayList<CacheResponseDTO>();

        for (Map.Entry<String, Set<Path>> entry : dto.getMap().entrySet()) {
            var entity = new CacheResponseDTO();

            entity.setQuery(entry.getKey());

            entity.setGifs(new ArrayList<>(entry.getValue()));

            result.add(entity);
        }

        return result;
    }
}
