package com.keep.simple.bsa1springboot.controller;

import com.keep.simple.bsa1springboot.service.MainService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.util.Set;


@RestController
public class GifsController {

    private final MainService mainService;

    public GifsController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/gifs")
    public Set<Path> getAll() {
        return mainService.findAllGifs();
    }

}
