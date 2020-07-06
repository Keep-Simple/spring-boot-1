package com.keep.simple.bsa1springboot.service;

import com.keep.simple.bsa1springboot.entities.Giph;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CacheService {
    // validate for username

    //return empty if not found
    public Optional<Giph> getGiphByQuery(String query) {
        return null;
    }

    public Giph save(Giph giph) {
        return null;
    }
}
