package com.webdev.cosmo.cosmobackend.service.internal.mail;

import com.webdev.cosmo.cosmobackend.service.internal.mail.service.MailSaveService;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.MailModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {
    private final MailSaveService mailSaveService;

    @PostMapping
    public MailModel save(@RequestBody MailModel mailModel) {
        return mailSaveService.save(mailModel);
    }
}
