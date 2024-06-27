package com.webdev.cosmo.cosmobackend.service.external.policy;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/api/user-privacy")
public class PolicyController {

    @RequestMapping("/policy")
    public String privacyPolicy(Model model) {
        return "privacy-policy";
    }

    @RequestMapping("/terms")
    public String termsOfService(Model model) {
        return "terms-of-service";
    }
}
