package com.webdev.cosmo.cosmobackend.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum Error {
    INVALID_IMAGE_DATA("Invalid image data", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST("Invalid request", HttpStatus.BAD_REQUEST),
    INVALID_FACEBOOK_PROFILE_DATA("Invalid profile data", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

    public ServiceError getError() {
        return new ServiceError(this);
    }


}
