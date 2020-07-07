package com.keep.simple.bsa1springboot.controller;

import com.keep.simple.bsa1springboot.dto.CacheDTO;
import com.keep.simple.bsa1springboot.service.CacheService;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CacheController {

    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @SneakyThrows
    @GetMapping("/cache")
    public CacheDTO getCache(@RequestParam(required = false) String query) {

        if (query != null) {
            return cacheService.getGiphsByQuery(query);
        }

        return cacheService.getAll();
    }
}
