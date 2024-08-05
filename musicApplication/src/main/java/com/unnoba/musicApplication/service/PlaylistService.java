package com.unnoba.musicApplication.service;

import com.unnoba.musicApplication.dto.PlaylistDTO;
import com.unnoba.musicApplication.dto.SongDTO;
import com.unnoba.musicApplication.exception.UserMismatchException;
import com.unnoba.musicApplication.exception.UserNotFoundException;
import com.unnoba.musicApplication.exception.PlaylistNotFoundException;
import com.unnoba.musicApplication.exception.SongNotFoundException;
import com.unnoba.musicApplication.model.Playlist;
import com.unnoba.musicApplication.model.Song;
import com.unnoba.musicApplication.model.auth.User;
import com.unnoba.musicApplication.repository.PlaylistRepository;
import com.unnoba.musicApplication.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongService songService;

    @Autowired
    private UserService userService;

    public List<PlaylistDTO> getAllPlaylists() {
        return convertToDTO(playlistRepository.findAll());
    }

    public List<PlaylistDTO> getMyPlaylists(String email) {
        User user = findUserByEmail(email);
        if (user == null)
            throw new UserNotFoundException(String.format("User not found with email: %s",  email));

        return convertToDTO(playlistRepository.findByUser(user));
    }

    private List<PlaylistDTO> convertToDTO(List<Playlist> playlists) {
        return playlists.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private PlaylistDTO convertToDTO(Playlist playlist) {
        return new PlaylistDTO(playlist.getName(), songService.convertToDTO(playlist.getSongs()));
    }

    public PlaylistDTO createPlaylist(PlaylistDTO playlistDTO, String email) {
        Playlist playlist = new Playlist();
        playlist.setName(playlistDTO.getName());
        playlist.setSongs(Collections.emptyList());
        playlist.setUser(findUserByEmail(email));
        return convertToDTO(playlistRepository.save(playlist));
    }


    private User findUserByEmail(String email) {
        User user = userService.find(email);
        if (user == null)
            throw new UserNotFoundException(String.format("User not found with email: %s",  email));
        return user;
    }

    public PlaylistDTO addSongToPlaylist(Long id, Long songId, String email) {
        Playlist playlist = playlistRepository.findById(id).orElse(null);
        if (playlist == null)
            throw new PlaylistNotFoundException(String.format("Playlist not found with id: %s",  id));

        User user = findUserByEmail(email);
        if (playlist.getUser() != null && !playlist.getUser().getId().equals(user.getId()))
            throw new UserMismatchException("Current user is not the owner of the playlist");
        if (playlist.getUser() == null) {
            playlist.setUser(user);
        }
        Song song = songService.findSongEntityById(songId);
        if (song == null)
            throw new SongNotFoundException(String.format("Song not found with id: %s",  songId));
        playlist.addSong(song);
        return convertToDTO(playlistRepository.save(playlist));
    }

    public PlaylistDTO removeSongFromPlaylist(Long id, Long songId, String email) {
        Playlist playlist = playlistRepository.findById(id).orElse(null);
        if (playlist == null)
            throw new PlaylistNotFoundException(String.format("Playlist not found with id: %s",  id));

        User user = findUserByEmail(email);
        if (playlist.getUser() != null && !playlist.getUser().getId().equals(user.getId()))
            throw new UserMismatchException("Current user is not the owner of the playlist");
        if (playlist.getUser() == null) {
            playlist.setUser(user);
        }
        Song song = songService.findSongEntityById(songId);
        if (song == null)
            throw new SongNotFoundException(String.format("Song not found with id: %s",  songId));
        playlist.removeSong(song);
        return convertToDTO(playlistRepository.save(playlist));
    }

    public PlaylistDTO updatePlaylistName(Long id, String name, String email) {
        Playlist playlist = playlistRepository.findById(id).orElse(null);
        if (playlist == null)
            throw new PlaylistNotFoundException(String.format("Playlist not found with id: %s",  id));

        User user = findUserByEmail(email);
        if (playlist.getUser() != null && !playlist.getUser().getId().equals(user.getId()))
            throw new UserMismatchException("Current user is not the owner of the playlist");
        if (playlist.getUser() == null) {
            playlist.setUser(user);
        }

        playlist.setName(name);
        return convertToDTO(playlistRepository.save(playlist));
    }

    public List<SongDTO> getSongsFromPlaylist(Long id) {
        Playlist playlist = playlistRepository.findById(id).orElse(null);
        if (playlist == null)
            throw new PlaylistNotFoundException(String.format("Playlist not found with id: %s",  id));
        return songService.convertToDTO(playlist.getSongs());
    }

    public PlaylistDTO deletePlaylist(Long id, String email) {
        Playlist playlist = playlistRepository.findById(id).orElse(null);
        if (playlist == null)
            throw new PlaylistNotFoundException(String.format("Playlist not found with id: %s",  id));

        User user = findUserByEmail(email);
        if (playlist.getUser() != null && !playlist.getUser().getId().equals(user.getId()))
            throw new UserMismatchException("Current user is not the owner of the playlist");
        if (playlist.getUser() == null) {
            playlist.setUser(user);
        }

        playlistRepository.deleteById(id);
        return convertToDTO(playlist);
    }
}
