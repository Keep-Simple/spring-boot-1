package com.keep.simple.bsa1springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserHistoryDTO {
    private String date;
    private String query;
    private String gif;
}
