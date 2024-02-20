package com.webdev.cosmo.cosmobackend.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ServiceError extends RuntimeException {
    private final Error error;
}
