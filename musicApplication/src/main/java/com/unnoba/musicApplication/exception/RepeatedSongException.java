package com.unnoba.musicApplication.exception;

public class RepeatedSongException extends RuntimeException {
    public RepeatedSongException(String message) {
        super(message);
    }
}
