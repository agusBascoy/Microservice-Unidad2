package com.unnoba.musicApplication.exception;

public class PlaylistNotFoundException extends RuntimeException {

    public PlaylistNotFoundException(String message) {
        super(message);
    }
}
