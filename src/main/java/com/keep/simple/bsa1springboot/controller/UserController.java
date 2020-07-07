package com.keep.simple.bsa1springboot.controller;

import com.keep.simple.bsa1springboot.service.DiskService;
import com.keep.simple.bsa1springboot.service.InMemoryCacheService;
import com.keep.simple.bsa1springboot.service.MainService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

@RestController
@RequestMapping("/user/")
public class UserController {

    private final MainService mainService;
    private final DiskService diskService;
    private final InMemoryCacheService ramService;

    public UserController(MainService mainService, DiskService diskService, InMemoryCacheService ramService) {
        this.mainService = mainService;
        this.diskService = diskService;
        this.ramService = ramService;
    }

    @PostMapping("{username}/generate")
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


    @GetMapping("{username}/search")
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

    @GetMapping("{username}/all")
    public ResponseEntity getUserData(@PathVariable String username) {

        if (!validate(username)) {
            return ResponseEntity.badRequest().body("Invalid username/query");
        }

        var result = diskService.getAllForUser(username);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("{username}/history")
    public ResponseEntity getUserHistory(@PathVariable String username) {

        if (!validate(username)) {
            return ResponseEntity.badRequest().body("Invalid username/query");
        }

        var result = diskService.readLog(username);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("{username}/history/clean")
    public void deleteUserHistory(@PathVariable String username) {
        diskService.clearLog(username);
    }

    @DeleteMapping("{username}/reset")
    public void deleteUserCache(
            @PathVariable String username,
            @RequestParam(required = false) String query) {

        if(query == null) {
            ramService.clearUser(username);
        } else {
            ramService.clearUserQuery(username, query);
        }
    }

    @DeleteMapping("{username}/clean")
    public void deleteUser(@PathVariable String username) {
        mainService.deleteUser(username);
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
