package com.webdev.cosmo.cosmobackend.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum Error {
    INVALID_IMAGE_DATA("Invalid image data", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST("Invalid request", HttpStatus.BAD_REQUEST),
    INVALID_USER_DATA("Invalid user data", HttpStatus.BAD_REQUEST),
    POST_NOT_FOUND("Post with given id not found", HttpStatus.NOT_FOUND),
    INVALID_POST_DATA("Invalid post data", HttpStatus.BAD_REQUEST),
    MAIL_SAVE_ERROR("Error when saving mail", HttpStatus.BAD_REQUEST),
    MAIL_HISTORY_EXISTS_ERROR("Mail history entry already exists", HttpStatus.CONFLICT),
    NO_TOKENS_FOUND("No tokens found in the database", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_ACCESS_TOKEN("Invalid access token, or insufficient permissions granted", HttpStatus.BAD_REQUEST),
    INVALID_CACHE_DATA("Invalid cache data, please validate it.", HttpStatus.INTERNAL_SERVER_ERROR),
    TOKEN_SAVE_ERROR("Error during token save", HttpStatus.BAD_REQUEST),
    NO_ACCESS_TOKEN_OR_USER_ID("No access token or user id", HttpStatus.UNAUTHORIZED),
    INVALID_API_KEY("Invalid api key", HttpStatus.UNAUTHORIZED),
    INVALID_WEBHOOK_VERIFY_TOKEN("Invalid webhook verify token", HttpStatus.FORBIDDEN),
    WEBHOOK_NOT_SUPPORTED("Value provided in changes is not supported.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

    public ServiceError getError() {
        return new ServiceError(this);
    }
}
