package com.keep.simple.bsa1springboot.controller;

import com.keep.simple.bsa1springboot.service.GiphRootService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.util.Set;


@RestController
public class GifsController {

    private final GiphRootService giphRootService;

    public GifsController(GiphRootService giphRootService) {
        this.giphRootService = giphRootService;
    }

    @GetMapping("/gifs")
    public Set<Path> getAll() {
        return giphRootService.findAllGifs();
    }

}
