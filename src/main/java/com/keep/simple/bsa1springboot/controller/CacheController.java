package com.keep.simple.bsa1springboot.controller;

import com.keep.simple.bsa1springboot.dto.DirsResponseDTO;
import com.keep.simple.bsa1springboot.service.CacheService;
import com.keep.simple.bsa1springboot.service.GiphService;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.keep.simple.bsa1springboot.helpers.Helper.validate;


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
    public ResponseEntity<List<DirsResponseDTO>> getCache(@RequestParam(required = false) String query) {

        if (query != null && !validate(query)) {
            return ResponseEntity.badRequest().build();
        }

        if (query != null) {
            return ResponseEntity.ok().body(cacheService.getGiphsByQuery(query));
        }

        return ResponseEntity.ok().body(cacheService.getAll());
    }

    @PostMapping("/cache/generate")
    public ResponseEntity<List<DirsResponseDTO>> postToCache(@RequestParam String query) {

        if (query != null && !validate(query)) {
            return ResponseEntity.badRequest().build();
        }

        var response = giphService.requestGif(query);

        if (response.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        cacheService.save(response.get(), query);

        return ResponseEntity.ok().body(cacheService.getGiphsByQuery(query));
    }

    @DeleteMapping("/cache")
    public void deleteCache() {
        cacheService.deleteAll();
    }
}
