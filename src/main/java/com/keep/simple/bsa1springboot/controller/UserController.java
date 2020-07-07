package com.keep.simple.bsa1springboot.controller;

import com.keep.simple.bsa1springboot.service.GiphService;
import com.keep.simple.bsa1springboot.service.MainService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;

import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

@RestController
public class UserController {

    private final MainService mainService;
    private final GiphService giphService;

    @Value("${api.header}")
    private String header;

    public UserController(MainService mainService, GiphService giphService) {
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
            return ResponseEntity.badRequest().body("Invalid username/query");
        }

        var result = mainService.generateGif(username, query, force);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .ok()
                .body(result.get().toUri().toString());
    }


    @GetMapping("/user/{username}/search")
    public ResponseEntity<String> getGif(
            @PathVariable String username,
            @RequestParam String query,
            @RequestParam(defaultValue = "false") Boolean force
    ) {

        if (!validate(username) || !validate(query)) {
            return ResponseEntity.badRequest().body("Invalid username/query");
        }

        var result = mainService.findGif(username, query, force);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .ok()
                .body(result.get().toUri().toString());
    }

    private static boolean validate(String value) {
        try {
            Paths.get(value);
            return true;
        } catch (InvalidPathException e) {
            return false;
        }
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<String> handleHeaderError(){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Header Missing");
    }

}
