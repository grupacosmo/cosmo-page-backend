package com.webdev.cosmo.cosmobackend.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class ErrorController {

    @ResponseBody
    @ExceptionHandler(ServiceError.class)
    public ResponseEntity<ErrorResponse> errorHandler(final ServiceError serviceError) {

        final Error error = serviceError.getError();
        log.error("Request failed: {} ({})", error.getMessage(), error.getHttpStatus().value());

        return ResponseEntity
                .status(error.getHttpStatus().value())
                .body(new ErrorResponse(error.getMessage(), error.getHttpStatus().value()));
    }

    @ResponseBody
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentNotValidException.class, ServletRequestBindingException.class, HandlerMethodValidationException.class, MethodArgumentTypeMismatchException.class, MissingPathVariableException.class})
    public ResponseEntity<ErrorResponse> badRequest(final Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Invalid request", HttpStatus.BAD_REQUEST.value()));
    }

    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> methodNotAllowed(final HttpRequestMethodNotSupportedException e) {
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ErrorResponse("Method not allowed", HttpStatus.METHOD_NOT_ALLOWED.value()));
    }

    @ResponseBody
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(final NoResourceFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Resource not found", HttpStatus.NOT_FOUND.value()));
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> internalError(final Exception e) {
        log.error("Unhandled exception", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}