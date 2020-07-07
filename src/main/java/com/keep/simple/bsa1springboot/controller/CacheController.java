package com.keep.simple.bsa1springboot.controller;

import com.keep.simple.bsa1springboot.dto.CacheDTO;
import com.keep.simple.bsa1springboot.service.CacheService;
import com.keep.simple.bsa1springboot.service.GiphService;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;


@RestController
public class CacheController {

    private final CacheService cacheService;
    private final GiphService giphService;

    public CacheController(CacheService cacheService, GiphService giphService) {
        this.cacheService = cacheService;
        this.giphService = giphService;
    }

    @SneakyThrows
    @GetMapping("/cache")
    public CacheDTO getCache(@RequestParam(required = false) String query) {

        if (query != null) {
            return cacheService.getGiphsByQuery(query);
        }

        return cacheService.getAll();
    }

    @PostMapping("/cache/generate")
    public CacheDTO postToCache(@RequestParam String query) {

        var response = giphService.requestGif(query);

        if (response.isEmpty()) {
            return new CacheDTO();
        }

        cacheService.save(response.get(), query);

        return cacheService.getGiphsByQuery(query);
    }

    @DeleteMapping("/cache")
    public void deleteCache() {
        cacheService.deleteAll();
    }
}
