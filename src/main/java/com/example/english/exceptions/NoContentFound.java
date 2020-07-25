package com.example.english.exceptions;

public class NoContentFound extends RuntimeException{

    public NoContentFound() {
        super();
    }

    public NoContentFound(String message) {
        super(message);
    }
}
