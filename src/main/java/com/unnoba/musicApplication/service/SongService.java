package com.unnoba.musicApplication.service;

import com.unnoba.musicApplication.dto.SongDTO;
import com.unnoba.musicApplication.model.Song;
import com.unnoba.musicApplication.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    public List<SongDTO> getAllSongs() {
        return convertToDTO(songRepository.findAll());
    }

    public List<SongDTO> convertToDTO(List<Song> songs) {
        return songs.stream().map(SongService::convertToDTO).collect(Collectors.toList());
    }

    private static SongDTO convertToDTO(Song song) {
        return new SongDTO(song.getName(), song.getAuthor(), song.getGenre());
    }

    public Song findSongEntityById(Long id) {
        if (id == null)
            return null;
        return songRepository.findById(id).orElse(null);
    }

    public SongDTO addSong(SongDTO songDTO) {
        Song song = new Song();
        song.setName(songDTO.getName());
        song.setAuthor(songDTO.getAuthor());
        song.setGenre(songDTO.getGenre());
        songRepository.save(song);
        return convertToDTO(song);
    }
}
