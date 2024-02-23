package com.webdev.cosmo.cosmobackend.service.external.facebook;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/facebook")
public class FacebookController {

    private final FacebookService facebookService;

    public FacebookController(FacebookService facebookService) {
        this.facebookService = facebookService;
    }

    @GetMapping("/posts")
    public Mono<String> getPosts(@RequestParam String pageId) {
        return facebookService.getPagePosts(pageId);
    }
}
