package com.keep.simple.bsa1springboot.service;

import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class InMemoryCacheService {

    private final HashMap<String, HashMap<String, Set<Path>>> cache = new HashMap<>();

    public void save(Path source, String username, String query) {
        source = source.toAbsolutePath();

        if (!cache.containsKey(username)) {
            var map = new HashMap<String, Set<Path>>();
            var ar = new HashSet<Path>();

            ar.add(source);
            map.put(query, ar);
            cache.put(username, map);
        }

        if (!cache.get(username).containsKey(query)) {
            var ar = new HashSet<Path>();

            ar.add(source);
            cache.get(username).put(query, ar);
        }

        cache.get(username).get(query).add(source);
    }

    public Optional<Path> getGiph(String query, String name) {
        if (cache.containsKey(name) && cache.get(name).containsKey(query)) {
            return cache.get(name).get(query).stream().findAny();
        }

        return Optional.empty();
    }

    public void clearUser(String username) {
        cache.remove(username);
    }

    public void clearUserQuery(String username, String query) {
        if (cache.containsKey(username)) {
            cache.get(username).remove(query);
        }
    }

}
