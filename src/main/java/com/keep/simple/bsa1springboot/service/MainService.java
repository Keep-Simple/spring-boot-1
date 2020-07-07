package com.keep.simple.bsa1springboot.service;

import com.keep.simple.bsa1springboot.controller.InMemoryCacheService;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.nio.file.Path;
import java.util.Optional;


@Service
public class MainService {

    private final DiskService diskService;
    private final GiphService giphService;
    private final CacheService cacheService;
    private final InMemoryCacheService ramService;

    public MainService(DiskService diskService, GiphService giphService, CacheService cacheService, InMemoryCacheService ramService) {
        this.diskService = diskService;
        this.giphService = giphService;
        this.cacheService = cacheService;
        this.ramService = ramService;
    }

    public Optional<Path> generateGif(String username, String query, boolean force) {
        Optional<Path> result = Optional.empty();

        if (!force) {
            result = cacheService.getGiphByQuery(query);
        }

        if (result.isEmpty()) {
            var response = giphService.requestGif(query);

            if (response.isEmpty()) {
                return Optional.empty();
            }

            result = cacheService.save(response.get(), query);
        }

        ramService.save(result.get(), username, query);

        return Optional.of(diskService.save(result.orElseThrow(), username, query));
    }

    public Optional<Path> findGif(String username, String query, boolean force) {
        Optional<Path> result;

        if (!force) {
            result = ramService.getGiph(query, username);

            if (result.isPresent()) return result;
        }

        result = diskService.getGiph(query, username);

        if (result.isEmpty()) {
            return Optional.empty();
        }

        result.ifPresent(path -> ramService.save(path, username, query));

        return result;
    }
}
