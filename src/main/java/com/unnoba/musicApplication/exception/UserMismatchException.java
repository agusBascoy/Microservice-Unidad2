package com.unnoba.musicApplication.exception;

public class UserMismatchException extends RuntimeException{

    public UserMismatchException(String message) {
        super(message);
    }

    public UserMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
