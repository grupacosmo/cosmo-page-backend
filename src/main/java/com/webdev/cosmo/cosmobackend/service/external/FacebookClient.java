package com.webdev.cosmo.cosmobackend.service.external;

import org.openapitools.model.AccessTokenRs;
import org.openapitools.model.FacebookResponse;
import org.openapitools.model.FacebookUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "facebookAccessTokenClient", url = "https://graph.facebook.com")
public interface FacebookClient {

    @GetMapping("/oauth/access_token")
    AccessTokenRs getAccessToken(@RequestParam("client_id") String clientId,
                                 @RequestParam("client_secret") String clientSecret,
                                 @RequestParam("grant_type") String grantType);

    @GetMapping("/{user-id}")
    FacebookUser verifyToken(@PathVariable("user-id") String userId,
                             @RequestParam("access_token") String accessToken,
                             @RequestParam(value = "fields", defaultValue = "id,name,email,picture") String fields);

    @GetMapping("/me/accounts")
    FacebookResponse getUserInfo(@RequestParam("access_token") String accessToken);


    @GetMapping("/{pageId}/feed")
    FacebookResponse getPostsPage(@PathVariable String pageId,
                        @RequestParam("access_token") String accessToken,
                                  @RequestParam(name = "limit", defaultValue = "100") Integer limit);

    @GetMapping("/{pageId}/feed")
    FacebookResponse subsequentRetrieve(@PathVariable String pageId,
                                        @RequestParam("access_token") String accessToken,
                                        @RequestParam(name = "limit", defaultValue = "100") Integer limit,
                                        @RequestParam("after") String after);

    @GetMapping("/{postId}")
    FacebookResponse getPostDetails(@PathVariable(name = "postId") String postId,
                                    @RequestParam("access_token") String accessToken);

    @GetMapping("/{postId}/attachments")
    FacebookResponse getPostAttachments(@PathVariable(name = "postId") String postId,
                                    @RequestParam("access_token") String accessToken);
}
