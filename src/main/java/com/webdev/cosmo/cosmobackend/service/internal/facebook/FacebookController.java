package com.webdev.cosmo.cosmobackend.service.internal.facebook;

import com.webdev.cosmo.cosmobackend.service.external.FacebookClient;
import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.async.Cache;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.FacebookResponse;
import org.openapitools.model.TokenModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.function.Consumer;

@RestController
@RequestMapping("/api/facebook")
@RequiredArgsConstructor
public class FacebookController {
    private final Consumer<TokenModel> saveTokenConsumer;
    private final FacebookClient facebookClient;
    private final Cache cache;

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveToken(@RequestBody TokenModel tokenModel) {
        saveTokenConsumer.accept(tokenModel);
    }

    @GetMapping("/accounts")
    public FacebookResponse smokeAccounts(@RequestParam String accessToken) {
        return facebookClient.getUserInfo(accessToken);
    }

    @GetMapping("/posts")
    public FacebookResponse smokePosts() {
        return facebookClient.getPostsPage(cache.getPageId(), cache.getPageAccessToken());
    }
}
