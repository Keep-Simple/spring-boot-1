package com.keep.simple.bsa1springboot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GiphResponseDto {
    private String url;
    private String id;

    @SuppressWarnings("unchecked")
    @JsonProperty("data")
    private void unpackNested(List<Map<String, Object>> data) {
        if (data.size() == 0) {
            throw new IllegalArgumentException("No Results found");
        }
            this.id = (String) data.get(0).get("id");
            Map<String, Map<String, Object>> gif = (Map<String, Map<String, Object>>) data.get(0).get("images");
            this.url = (String) gif.get("downsized_large").get("url");
    }
}
