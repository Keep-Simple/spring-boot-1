package com.keep.simple.bsa1springboot.controller;

import com.keep.simple.bsa1springboot.service.GiphService;
import com.keep.simple.bsa1springboot.service.MainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

@RestController
@Slf4j
public class MainController {

    private final MainService mainService;
    private final GiphService giphService;

    public MainController(MainService mainService, GiphService giphService) {
        this.mainService = mainService;
        this.giphService = giphService;
    }

    @GetMapping("/get/{username}/{query}")
    public ResponseEntity<String> getGiph(@PathVariable String username, @PathVariable String query) {
        if (!validate(username) || !validate(query)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username/query");
        }

            return ResponseEntity.status(HttpStatus.OK).body(mainService.generateGif(username, query));
    }

    @GetMapping("/gifs")
    public ResponseEntity<String> getAllGiphsFromDisk() {
        return ResponseEntity.status(HttpStatus.OK).body(giphService.getAll());
    }

    private boolean validate(String value) {
        try {
            Paths.get(value);
            return true;
        } catch (InvalidPathException e) {
            return false;
        }
    }

}
