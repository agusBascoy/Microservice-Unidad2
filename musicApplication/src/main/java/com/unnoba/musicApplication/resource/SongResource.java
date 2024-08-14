package com.unnoba.musicApplication.resource;

import com.unnoba.musicApplication.dto.SongDTO;
import com.unnoba.musicApplication.exception.RepeatedSongException;
import com.unnoba.musicApplication.model.Genre;
import com.unnoba.musicApplication.service.SongService;
import com.unnoba.musicApplication.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/song")
public class SongResource {

    @Autowired
    private SongService songService;


    @PostMapping(value = "/create", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SongDTO> createSong(@RequestHeader("Authorization") String authToken, @RequestBody SongDTO songDTO) {
        String user = JwtUtil.extractSubject(authToken);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (songDTO == null || songDTO.getName() == null || songDTO.getAuthor() == null || songDTO.getGenre() == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            return ResponseEntity.ok(songService.addSong(songDTO));
        } catch (RepeatedSongException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SongDTO>> getAllSongs(@RequestParam(required = false, name = "author") String author,
                                                     @RequestParam(required = false, name = "genre") String genre) {
        Genre genreEnum = null;
        if (genre != null ) {
            genreEnum = Genre.valueOf(genre.toUpperCase());
        }
        return ResponseEntity.ok(songService.getAllSongs(genreEnum, author));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteSongs(@RequestHeader("Authorization") String authToken,
                                              @RequestBody List<Long> ids) {
        String user = JwtUtil.extractSubject(authToken);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (CollectionUtils.isEmpty(ids)) {
            return ResponseEntity.badRequest().build();
        }
        songService.deleteSongs(ids);
        return ResponseEntity.ok("Song deleted");
    }
}
