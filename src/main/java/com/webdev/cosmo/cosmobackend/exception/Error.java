package com.webdev.cosmo.cosmobackend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum Error {
    INVALID_DATA("A", "A", HttpStatus.BAD_REQUEST);

    private final String message;
    private final String code;
    private final HttpStatus httpStatus;
}
