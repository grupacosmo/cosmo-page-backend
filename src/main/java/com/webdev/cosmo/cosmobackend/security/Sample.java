package com.webdev.cosmo.cosmobackend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Sample {
    @GetMapping(path = "/user")
    public String test() {
        return "DUPA DUPA 123";
    }
}
