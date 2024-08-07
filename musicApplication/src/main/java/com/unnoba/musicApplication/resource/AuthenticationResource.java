package com.unnoba.musicApplication.resource;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.unnoba.musicApplication.configuration.SecurityConstants;
import com.unnoba.musicApplication.dto.UserDTO;
import com.unnoba.musicApplication.model.auth.User;
import com.unnoba.musicApplication.service.auth.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthenticationResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> auth(@RequestBody UserDTO userDTO,  HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword())
        );

        UserDetails user = (UserDetails) authentication.getPrincipal();
        String token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

        response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);

        return ResponseEntity.ok(token);
    }

    @PostMapping(value = "/register", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        if (userDTO.getEmail() == null || userDTO.getPassword() == null) {
            return new ResponseEntity<>("Invalid data", HttpStatus.BAD_REQUEST);
        }

        User existingUser = userService.find(userDTO.getEmail());
        if (existingUser != null) {
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        }

        User newUser = new User();
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword(userDTO.getPassword());

        userService.saveUser(newUser);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

}
