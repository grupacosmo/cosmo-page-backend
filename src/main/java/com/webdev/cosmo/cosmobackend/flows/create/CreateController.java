package com.webdev.cosmo.cosmobackend.flows.create;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/create")
public class CreateController {
    private final Dispatcher<> createFlowDispatcher;

    @PostMapping
    public void saveImage() {

    }
}
