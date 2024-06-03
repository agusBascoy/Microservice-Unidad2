package com.unnoba.musicApplication.repository;

import com.unnoba.musicApplication.model.Genre;
import com.unnoba.musicApplication.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    List<Song> findByGenreAndAuthor(Genre genre, String author);

    List<Song> findByGenre(Genre genre);

    List<Song> findByAuthor(String author);
}
