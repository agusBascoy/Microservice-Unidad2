package com.unnoba.musicApplication.service.auth;

import com.unnoba.musicApplication.model.auth.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User saveUser(User user);

    User find(String email);
}
