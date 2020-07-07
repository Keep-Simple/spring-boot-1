package com.keep.simple.bsa1springboot.service;

import com.keep.simple.bsa1springboot.controller.InMemoryCacheService;
import org.springframework.stereotype.Service;

import java.net.URI;


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
}
