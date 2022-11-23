package com.example.demo.exception;

public class NotHaveFileException extends RuntimeException {
    public NotHaveFileException(String message) {
        super(message);
    }
}
