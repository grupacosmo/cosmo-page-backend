package com.webdev.cosmo.cosmobackend.error;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Type;

@Getter
@RequiredArgsConstructor
public enum Error {
    INVALID_IMAGE_DATA("Invalid image data", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST("Invalid request", HttpStatus.BAD_REQUEST),
    INVALID_USER_DATA("Invalid user data", HttpStatus.BAD_REQUEST),
    POST_NOT_FOUND("Post with given id not found", HttpStatus.NOT_FOUND),
    INVALID_POST_DATA("Invalid post data", HttpStatus.BAD_REQUEST),
    MAIL_SAVE_ERROR("Error when saving mail", HttpStatus.BAD_REQUEST),
    MAIL_HISTORY_EXISTS_ERROR("Error when saving mail", HttpStatus.CONFLICT),
    NO_TOKENS_FOUND("No tokens found in the database", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_ACCESS_TOKEN("Invalid access token, or insufficient permissions granted", HttpStatus.BAD_REQUEST),
    INVALID_CACHE_DATA("Invalid cache data, please validate it.", HttpStatus.INTERNAL_SERVER_ERROR),
    TOKEN_SAVE_ERROR("Error during token save", HttpStatus.BAD_REQUEST),
    NO_ACCESS_TOKEN_OR_USER_ID("No access token or user id", HttpStatus.UNAUTHORIZED);


    private final String message;
    private final HttpStatus httpStatus;

    public ServiceError getError() {
        return new ServiceError(this);
    }

    public static class Serializer implements JsonSerializer<Error> {
        @Override
        public JsonElement serialize(Error error, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("message", error.getMessage());
            jsonObject.addProperty("httpStatus", error.getHttpStatus().value());
            return jsonObject;
        }
    }

}
