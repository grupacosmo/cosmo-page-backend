package com.webdev.cosmo.cosmobackend.service.internal.mail;

import com.webdev.cosmo.cosmobackend.service.internal.mail.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

import java.util.Map;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {
    private final EmailService emailService;

    @SneakyThrows
    @GetMapping()
    public void smoke() {
        Context context = new Context();
        context.setVariables(Map.of("email", "jamr.mat@gmail.com"));

        emailService.sendHtmlMail("jamr.mat@gmail.com", "Subject Here", "accountCreate", context);
    }
}
