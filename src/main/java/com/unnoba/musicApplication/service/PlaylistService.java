package com.unnoba.musicApplication.service;

import com.unnoba.musicApplication.dto.PlaylistDTO;
import com.unnoba.musicApplication.dto.SongDTO;
import com.unnoba.musicApplication.exception.PlaylistNotFoundException;
import com.unnoba.musicApplication.exception.SongNotFoundException;
import com.unnoba.musicApplication.model.Playlist;
import com.unnoba.musicApplication.model.Song;
import com.unnoba.musicApplication.repository.PlaylistRepository;
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

    public List<PlaylistDTO> getAllPlaylists() {
        return convertToDTO(playlistRepository.findAll());
    }

    private List<PlaylistDTO> convertToDTO(List<Playlist> playlists) {
        return playlists.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private PlaylistDTO convertToDTO(Playlist playlist) {
        return new PlaylistDTO(playlist.getName(), songService.convertToDTO(playlist.getSongs()));
    }

    public PlaylistDTO createPlaylist(PlaylistDTO playlistDTO) {
        Playlist playlist = new Playlist();
        playlist.setName(playlistDTO.getName());
        playlist.setSongs(Collections.emptyList());
        return convertToDTO(playlistRepository.save(playlist));
    }

    public PlaylistDTO addSongToPlaylist(Long id, Long songId) {
        Playlist playlist = playlistRepository.findById(id).orElse(null);
        if (playlist == null)
            throw new PlaylistNotFoundException(String.format("Playlist not found with id: %s",  id));

        Song song = songService.findSongEntityById(songId);
        if (song == null)
            throw new SongNotFoundException(String.format("Song not found with id: %s",  songId));
        playlist.addSong(song);
        return convertToDTO(playlistRepository.save(playlist));
    }

    public PlaylistDTO removeSongFromPlaylist(Long id, Long songId) {
        Playlist playlist = playlistRepository.findById(id).orElse(null);
        if (playlist == null)
            throw new PlaylistNotFoundException(String.format("Playlist not found with id: %s",  id));

        Song song = songService.findSongEntityById(songId);
        if (song == null)
            throw new SongNotFoundException(String.format("Song not found with id: %s",  songId));
        playlist.removeSong(song);
        return convertToDTO(playlistRepository.save(playlist));
    }

    public PlaylistDTO updatePlaylistName(Long id, String name) {
        Playlist playlist = playlistRepository.findById(id).orElse(null);
        if (playlist == null)
            throw new PlaylistNotFoundException(String.format("Playlist not found with id: %s",  id));
        playlist.setName(name);
        return convertToDTO(playlistRepository.save(playlist));
    }

    public List<SongDTO> getSongsFromPlaylist(Long id) {
        Playlist playlist = playlistRepository.findById(id).orElse(null);
        if (playlist == null)
            throw new PlaylistNotFoundException(String.format("Playlist not found with id: %s",  id));
        return songService.convertToDTO(playlist.getSongs());
    }

    //Delete playlist by id returning playlistDTO removed, and check if playlist exists with the id sent
    public PlaylistDTO deletePlaylist(Long id) {
        Playlist playlist = playlistRepository.findById(id).orElse(null);
        if (playlist == null)
            throw new PlaylistNotFoundException(String.format("Playlist not found with id: %s",  id));
        playlistRepository.deleteById(id);
        return convertToDTO(playlist);
    }
}
