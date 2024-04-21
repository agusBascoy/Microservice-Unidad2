package com.unnoba.musicApplication.resource;

import com.unnoba.musicApplication.dto.SongDTO;
import com.unnoba.musicApplication.service.SongService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/song")
public class SongResource {

    @Autowired
    private SongService songService;


    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SongDTO> createSong(SongDTO songDTO) {
        if (songDTO == null || songDTO.getName() == null || songDTO.getAuthor() == null || songDTO.getGenre() == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(songService.addSong(songDTO));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SongDTO>> getAllSongs() {
        return ResponseEntity.ok(songService.getAllSongs());
    }


}
