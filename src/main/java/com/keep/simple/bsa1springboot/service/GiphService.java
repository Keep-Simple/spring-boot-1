package com.keep.simple.bsa1springboot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keep.simple.bsa1springboot.dto.GiphResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;


@Service
public class GiphService {
    @Value("${api.giphy.search}")
    private String url;

    public Optional<GiphResponseDto> requestGif(String query) {
        try {
            var response = HttpClient.newHttpClient().send(buildGetRequest(query), HttpResponse.BodyHandlers.ofString());
            var mapper = new ObjectMapper();

            GiphResponseDto giph = mapper.readValue(response.body(), GiphResponseDto.class);

            return Optional.of(giph);

        } catch (IOException | InterruptedException| IllegalArgumentException ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
    }

    private HttpRequest buildGetRequest(String query) {
        return HttpRequest
                .newBuilder()
                .uri(URI.create(url + "&q=" + query))
                .GET()
                .build();
    }
}
