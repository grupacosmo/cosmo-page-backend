package com.webdev.cosmo.cosmobackend.service.external.facebook.external;

import org.openapitools.model.AccessTokenRs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "facebookAccessTokenClient", url = "https://graph.facebook.com")
public interface FacebookAccessTokenClient {

    @GetMapping("/oauth/access_token")
    AccessTokenRs getAccessToken(@RequestParam("client_id") String clientId,
                                 @RequestParam("client_secret") String clientSecret,
                                 @RequestParam("grant_type") String grantType);
}
