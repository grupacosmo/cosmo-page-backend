package com.webdev.cosmo.cosmobackend.service.internal.mail;

import com.webdev.cosmo.cosmobackend.service.internal.mail.service.MailSenderConsumer;
import com.webdev.cosmo.cosmobackend.util.templates.SaveService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.openapitools.model.MailModel;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import java.util.Map;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {
    private final MailSenderConsumer mailSenderConsumer;
    private final SaveService<MailModel, MailModel> mailSaveService;

    @PostMapping
    public MailModel save(@RequestBody MailModel mailModel) {
        return mailSaveService.save(mailModel);
    }

    @SneakyThrows
    @GetMapping()
    public void smoke() {
        mailSenderConsumer.accept(new MailContext()
                .setRecipient("jamr.mat@gmail.com")
                .setSubject("Halo halo")
                .setTemplateName("accountCreate")
                .setArgs(Map.of("email", "jamr.mat@gmail.com")));
    }
}
