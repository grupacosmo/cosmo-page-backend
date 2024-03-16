package com.webdev.cosmo.cosmobackend.service.external;

import org.openapitools.model.FacebookUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "facebook-client", url = "https://graph.facebook.com/")
public interface FacebookClient {

    @RequestMapping(method = RequestMethod.GET, value = "/{user-id}")
    FacebookUser verifyToken(@PathVariable("user-id") String userId,
                             @RequestParam("access_token") String accessToken,
                             @RequestParam(value = "fields", defaultValue = "id,name,email,picture") String fields);
}
