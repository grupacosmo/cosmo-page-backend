package com.webdev.cosmo.cosmobackend.service.internal.facebook;

import com.webdev.cosmo.cosmobackend.service.internal.facebook.service.FacebookService;
import org.openapitools.model.InternalPageSecretModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/facebook")
public class FacebookController {

    private final FacebookService facebookService;

    public FacebookController(FacebookService facebookService) {
        this.facebookService = facebookService;
    }

    @PostMapping("/token")
    public InternalPageSecretModel createFbProfile(@RequestBody InternalPageSecretModel facebookInternalProfileModel) {
        return facebookService.createNewPassword(facebookInternalProfileModel);
    }
}
