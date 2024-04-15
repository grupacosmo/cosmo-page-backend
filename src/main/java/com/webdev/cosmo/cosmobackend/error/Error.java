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
