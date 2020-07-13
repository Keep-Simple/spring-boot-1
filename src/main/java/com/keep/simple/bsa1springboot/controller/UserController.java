package com.keep.simple.bsa1springboot.controller;

import com.keep.simple.bsa1springboot.dto.DirsResponseDTO;
import com.keep.simple.bsa1springboot.dto.UserHistoryDTO;
import com.keep.simple.bsa1springboot.service.DiskService;
import com.keep.simple.bsa1springboot.service.InMemoryCacheService;
import com.keep.simple.bsa1springboot.service.GiphRootService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.keep.simple.bsa1springboot.helpers.Helper.validate;

@RestController
@RequestMapping("/user/")
public class UserController {

    private final GiphRootService giphRootService;
    private final DiskService diskService;
    private final InMemoryCacheService ramService;

    public UserController(GiphRootService giphRootService, DiskService diskService, InMemoryCacheService ramService) {
        this.giphRootService = giphRootService;
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

        var result = giphRootService.generateGif(username, query, force);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(result.get().toUri().toString());
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

        var result = giphRootService.findGif(username, query, force);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(result.get().toUri().toString());
    }

    @GetMapping("{username}/all")
    public ResponseEntity<List<DirsResponseDTO>> getUserData(@PathVariable String username) {

        if (!validate(username)) {
            return ResponseEntity.badRequest().build();
        }

        var result = diskService.getAllForUser(username);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("{username}/history")
    public ResponseEntity<List<UserHistoryDTO>> getUserHistory(@PathVariable String username) {

        if (!validate(username)) {
            return ResponseEntity.badRequest().build();
        }

        var result = diskService.readLog(username);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("{username}/history/clean")
    public void deleteUserHistory(@PathVariable String username) {
        if (validate(username)) {
            diskService.clearLog(username);
        }
    }

    @DeleteMapping("{username}/reset")
    public void deleteUserCache(
            @PathVariable String username,
            @RequestParam(required = false) String query) {

        if (validate(username)) {
            if (query == null) {
                ramService.clearUser(username);
            } else {
                ramService.clearUserQuery(username, query);
            }
        }
    }

    @DeleteMapping("{username}/clean")
    public void deleteUser(@PathVariable String username) {
        if (validate(username)) {
            giphRootService.deleteUser(username);
        }
    }

}
