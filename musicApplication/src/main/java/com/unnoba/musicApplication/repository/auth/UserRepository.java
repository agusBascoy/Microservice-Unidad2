package com.unnoba.musicApplication.repository.auth;

import com.unnoba.musicApplication.model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByEmail(String email);
}
