package com.unnoba.musicApplication.dto;

import com.unnoba.musicApplication.model.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SongDTO {

    private String name;

    private String author;

    private Genre genre;
}
