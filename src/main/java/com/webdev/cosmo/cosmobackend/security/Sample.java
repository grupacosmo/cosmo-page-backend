package com.webdev.cosmo.cosmobackend.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Sample {
    @GetMapping(path = "/user")
    public String test() {
        return "DUPA DUPA 123";
    }

    @GetMapping("/unauthenticated")
    public String test2(){
        return "Unauthenticated, welcome!";
    }

}
