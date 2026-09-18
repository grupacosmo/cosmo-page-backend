package com.webdev.cosmo.cosmobackend.error;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorControllerTest {

    private final ErrorController controller = new ErrorController();

    @Test
    void mapsServiceErrorToItsDeclaredStatus() {
        ResponseEntity<ErrorResponse> response = controller.errorHandler(Error.POST_NOT_FOUND.getError());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().httpStatus()).isEqualTo(404);
    }

    @Test
    void mapsTypeMismatchToBadRequest() {
        ResponseEntity<ErrorResponse> response = controller.badRequest(
                new MethodArgumentTypeMismatchException("x", int.class, "page", null, null));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void mapsUnsupportedMethodToMethodNotAllowed() {
        ResponseEntity<ErrorResponse> response = controller.methodNotAllowed(
                new HttpRequestMethodNotSupportedException("POST"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Test
    void mapsUnknownExceptionToInternalServerError() {
        ResponseEntity<ErrorResponse> response = controller.internalError(new IllegalStateException("boom"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().message()).isEqualTo("Internal server error");
    }
}