package com.webdev.cosmo.cosmobackend.posts.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
