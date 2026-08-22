package com.webdev.cosmo.cosmobackend.error;

public record ErrorResponse(String message, int httpStatus) {
}