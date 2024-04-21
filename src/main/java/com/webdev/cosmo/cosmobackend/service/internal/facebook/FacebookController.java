package com.webdev.cosmo.cosmobackend.service.internal.facebook;

import lombok.RequiredArgsConstructor;
import org.openapitools.model.TokenModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.function.Consumer;

@RestController
@RequestMapping("/api/facebook")
@RequiredArgsConstructor
public class FacebookController {
    private final Consumer<TokenModel> saveTokenConsumer;

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveToken(@RequestBody TokenModel tokenModel) {
        saveTokenConsumer.accept(tokenModel);
    }
}
