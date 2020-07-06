package com.keep.simple.bsa1springboot.service;

import com.keep.simple.bsa1springboot.controller.InMemoryCacheService;
import org.springframework.stereotype.Service;

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

    public String generateGif(String username, String query) {

        var result = cacheService.getGiphByQuery(query);

//        if (result.isEmpty()) {
            var response = giphService.requestGif(query);
//
//            result = Optional.of(cacheService.save(response));
//        }

//        diskService.save(result.get(), username);
//        ramService.save(result.get(), username);

    return null;}


}
