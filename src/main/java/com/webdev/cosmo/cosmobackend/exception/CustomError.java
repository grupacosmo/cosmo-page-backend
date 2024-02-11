package com.webdev.cosmo.cosmobackend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomError extends RuntimeException {
    private final Error error;
}
