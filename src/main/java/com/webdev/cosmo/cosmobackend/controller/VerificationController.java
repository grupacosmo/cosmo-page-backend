package com.webdev.cosmo.cosmobackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/verification")
public class VerificationController {

    @GetMapping("/unsecured")
    public String getUnsecuredEndpoint(){
        return "Unsecured";
    }

    @GetMapping("/secured")
    public String getSecuredEndpoint(){
        return "Secured content";
    }

}
