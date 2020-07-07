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

    public URI generateGif(String username, String query, boolean force) {

        var result = cacheService.getGiphByQuery(query);

        if (result.isEmpty()) {
            var response = giphService.requestGif(query);

            if (response.isEmpty()) {
                return URI.create("Nothing_found");
            }

            result = cacheService.save(response.get(), query);
        }

        ramService.save(result.get(), username, query);

        return diskService.save(result.orElseThrow(), username, query).toUri();
    }

    public URI findGif(String username, String query, boolean force) {
        Optional<Path> result;

        if (!force) {
            result = ramService.getGiph(query, username);

            if (result.isPresent()) return result.get().toUri();
        }

        result = diskService.getGiph(query, username);

        if (result.isEmpty()) {
            return URI.create("Nothing_found");
        }

        result.ifPresent(path -> ramService.save(path, username, query));

        return result.get().toUri();
    }
}
