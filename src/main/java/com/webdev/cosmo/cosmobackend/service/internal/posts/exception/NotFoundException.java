package com.webdev.cosmo.cosmobackend.service.internal.posts.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
