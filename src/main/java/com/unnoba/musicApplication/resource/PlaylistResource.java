package com.unnoba.musicApplication.resource;

import com.unnoba.musicApplication.dto.PlaylistDTO;
import com.unnoba.musicApplication.dto.SongDTO;
import com.unnoba.musicApplication.exception.PlaylistNotFoundException;
import com.unnoba.musicApplication.exception.SongNotFoundException;
import com.unnoba.musicApplication.service.PlaylistService;
import jakarta.ws.rs.QueryParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/playlist")
public class PlaylistResource {

    @Autowired
    private PlaylistService playlistService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PlaylistDTO>> getAllPlaylists() {
        return ResponseEntity.ok(playlistService.getAllPlaylists());
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlaylistDTO> createPlaylist(PlaylistDTO playlistDTO) {
        if (playlistDTO == null || StringUtils.isEmpty(playlistDTO.getName()))
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(playlistService.createPlaylist(playlistDTO));
    }

    @RequestMapping(value = "/{id}/song", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlaylistDTO> addSongToPlaylist(@PathVariable Long id,
                                                         @QueryParam("songId") Long songId) {
        if (id == null || songId == null)
            return ResponseEntity.badRequest().build();

        try {
            return ResponseEntity.ok(playlistService.addSongToPlaylist(id, songId));
        } catch (PlaylistNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (SongNotFoundException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/{id}/song", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlaylistDTO> removeSongFromPlaylist(@PathVariable Long id,
                                                              @QueryParam("songId") Long songId) {
        if (id == null || songId == null)
            return ResponseEntity.badRequest().build();

        try {
            return ResponseEntity.ok(playlistService.removeSongFromPlaylist(id, songId));
        } catch (PlaylistNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (SongNotFoundException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(value = "/{id}/name", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlaylistDTO> updatePlaylistName(@PathVariable Long id,
                                                          @QueryParam("name") String name) {
        if (id == null || StringUtils.isEmpty(name))
            return ResponseEntity.badRequest().build();

        try {
            return ResponseEntity.ok(playlistService.updatePlaylistName(id, name));
        } catch (PlaylistNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/{id}/songs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SongDTO>> getSongsFromPlaylist(@PathVariable Long id) {
        if (id == null)
            return ResponseEntity.badRequest().build();

        try {
            return ResponseEntity.ok(playlistService.getSongsFromPlaylist(id));
        } catch (PlaylistNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<PlaylistDTO> deletePlaylist(@PathVariable Long id) {
        if (id == null)
            return ResponseEntity.badRequest().build();

        try {
            return ResponseEntity.ok(playlistService.deletePlaylist(id));
        } catch (PlaylistNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
