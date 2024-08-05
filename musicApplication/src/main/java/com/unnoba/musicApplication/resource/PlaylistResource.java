package com.unnoba.musicApplication.resource;

import com.unnoba.musicApplication.dto.PlaylistDTO;
import com.unnoba.musicApplication.dto.SongDTO;
import com.unnoba.musicApplication.exception.PlaylistNotFoundException;
import com.unnoba.musicApplication.exception.SongNotFoundException;
import com.unnoba.musicApplication.exception.UserMismatchException;
import com.unnoba.musicApplication.exception.UserNotFoundException;
import com.unnoba.musicApplication.service.PlaylistService;
import com.unnoba.musicApplication.util.JwtUtil;
import jakarta.ws.rs.QueryParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/playlist")
@CrossOrigin
public class PlaylistResource {

    @Autowired
    private PlaylistService playlistService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PlaylistDTO>> getAllPlaylists() {
        return ResponseEntity.ok(playlistService.getAllPlaylists());
    }

    @RequestMapping(value = "/myPlaylists", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PlaylistDTO>> getMyPlaylists(@RequestHeader("Authorization") String authToken) {
        String user = JwtUtil.extractSubject(authToken);
        if (StringUtils.isEmpty(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(playlistService.getMyPlaylists(user));
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlaylistDTO> createPlaylist(@RequestHeader("Authorization") String authToken, @RequestBody PlaylistDTO playlistDTO) {
        String user = JwtUtil.extractSubject(authToken);
        if (StringUtils.isEmpty(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (playlistDTO == null || StringUtils.isEmpty(playlistDTO.getName()))
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(playlistService.createPlaylist(playlistDTO, user));
    }

    @RequestMapping(value = "/{id}/song", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlaylistDTO> addSongToPlaylist(@RequestHeader("Authorization") String authToken, @PathVariable Long id,
                                                         @QueryParam("songId") Long songId) {
        String user = JwtUtil.extractSubject(authToken);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (id == null || songId == null)
            return ResponseEntity.badRequest().build();

        try {
            return ResponseEntity.ok(playlistService.addSongToPlaylist(id, songId, user));
        } catch (PlaylistNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (SongNotFoundException ex) {
            return ResponseEntity.badRequest().build();
        } catch (UserMismatchException ex) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        } catch (UserNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @RequestMapping(value = "/{id}/song", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlaylistDTO> removeSongFromPlaylist(@RequestHeader("Authorization") String authToken, @PathVariable Long id,
                                                              @QueryParam("songId") Long songId) {
        String user = JwtUtil.extractSubject(authToken);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (id == null || songId == null)
            return ResponseEntity.badRequest().build();

        try {
            return ResponseEntity.ok(playlistService.removeSongFromPlaylist(id, songId, user));
        } catch (PlaylistNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (SongNotFoundException ex) {
            return ResponseEntity.badRequest().build();
        } catch (UserMismatchException ex) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        } catch (UserNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @RequestMapping(value = "/{id}/name", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlaylistDTO> updatePlaylistName(@RequestHeader("Authorization") String authToken, @PathVariable Long id,
                                                          @QueryParam("name") String name) {
        String user = JwtUtil.extractSubject(authToken);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (id == null || StringUtils.isEmpty(name))
            return ResponseEntity.badRequest().build();

        try {
            return ResponseEntity.ok(playlistService.updatePlaylistName(id, name, user));
        } catch (PlaylistNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (UserMismatchException ex) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        } catch (UserNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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
    public ResponseEntity<PlaylistDTO> deletePlaylist(@RequestHeader("Authorization") String authToken, @PathVariable Long id) {
        String user = JwtUtil.extractSubject(authToken);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (id == null)
            return ResponseEntity.badRequest().build();

        try {
            return ResponseEntity.ok(playlistService.deletePlaylist(id, user));
        } catch (PlaylistNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (UserMismatchException ex) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        } catch (UserNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
