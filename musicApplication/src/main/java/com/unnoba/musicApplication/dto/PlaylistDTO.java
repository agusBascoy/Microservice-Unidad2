package com.unnoba.musicApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PlaylistDTO {

    private Long id;

    private String name;

    private List<SongDTO> songDTOS;
}
