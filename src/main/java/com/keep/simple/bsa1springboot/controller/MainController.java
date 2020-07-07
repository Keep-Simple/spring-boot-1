package com.keep.simple.bsa1springboot.controller;

import com.keep.simple.bsa1springboot.service.GiphService;
import com.keep.simple.bsa1springboot.service.MainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import static com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat.URI;

@RestController
@Slf4j
public class MainController {

    private final MainService mainService;
    private final GiphService giphService;

    public MainController(MainService mainService, GiphService giphService) {
        this.mainService = mainService;
        this.giphService = giphService;
    }

    @PostMapping("/user/{username}/generate")
    public ResponseEntity<String> generateOrGetGif(
            @PathVariable String username,
            @RequestParam String query,
            @RequestParam(defaultValue = "false") Boolean force
    ) {

        if (!validate(username) || !validate(query)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username/query");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mainService.generateGif(username, query, force).toString());
    }


    @GetMapping("/user/{username}/search")
    public ResponseEntity<String> getGif(
            @PathVariable String username,
            @RequestParam String query,
            @RequestParam(defaultValue = "false") Boolean force
    ) {

        if (!validate(username) || !validate(query)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username/query");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mainService.findGif(username, query, force).toString());
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
