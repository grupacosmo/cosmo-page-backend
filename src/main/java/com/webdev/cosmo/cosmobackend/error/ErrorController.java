package com.webdev.cosmo.cosmobackend.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class ErrorController {

    @ResponseBody
    @ExceptionHandler(ServiceError.class)
    public ResponseEntity<Error> errorHandler(final ServiceError serviceError) {

        final Error error = serviceError.getError();
        log.error(error.getMessage());

        return ResponseEntity
                .status(error.getHttpStatus().value())
                .body(error);
    }
}
