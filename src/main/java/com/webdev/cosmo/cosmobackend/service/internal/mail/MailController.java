package com.webdev.cosmo.cosmobackend.service.internal.mail;

import com.webdev.cosmo.cosmobackend.util.interfaces.SaveService;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.MailModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {
    private final SaveService<MailModel, MailModel> mailSaveService;

    @PostMapping
    public MailModel save(@RequestBody MailModel mailModel) {
        return mailSaveService.save(mailModel);
    }
}
