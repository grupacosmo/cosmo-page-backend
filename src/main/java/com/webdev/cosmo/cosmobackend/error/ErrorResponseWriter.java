package com.webdev.cosmo.cosmobackend.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ErrorResponseWriter {

    private final ObjectMapper objectMapper;

    public void write(HttpServletResponse response, Error error) throws IOException {
        response.setStatus(error.getHttpStatus().value());
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), new ErrorResponse(error.getMessage(), error.getHttpStatus().value()));
    }
}