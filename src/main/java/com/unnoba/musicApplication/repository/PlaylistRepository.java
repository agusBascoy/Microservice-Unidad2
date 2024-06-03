package com.unnoba.musicApplication.repository;

import com.unnoba.musicApplication.model.Playlist;
import com.unnoba.musicApplication.model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    List<Playlist> findByUser(User user);
}
