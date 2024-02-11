package com.webdev.cosmo.cosmobackend.exception;

import com.webdev.cosmo.cosmobackend.models.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(CustomError.class)
    public ResponseEntity<ErrorResponse> handleBookNotFoundException(CustomError ex) {
        ErrorResponse response = new ErrorResponse()
                .message(ex.getError().getMessage())
                .code(ex.getError().getCode());

        return new ResponseEntity<>(response, ex.getError().getHttpStatus());
    }

}
