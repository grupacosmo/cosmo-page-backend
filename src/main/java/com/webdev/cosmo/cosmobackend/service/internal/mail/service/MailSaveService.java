package com.webdev.cosmo.cosmobackend.service.internal.mail.service;

import com.webdev.cosmo.cosmobackend.service.api.Mail;
import com.webdev.cosmo.cosmobackend.service.internal.mail.mapper.MailMapper;
import com.webdev.cosmo.cosmobackend.service.internal.mail.repository.MailRepository;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.MailModel;

import java.time.OffsetDateTime;

@RequiredArgsConstructor
public class MailSaveService {
    private final MailRepository mailRepository;
    private final MailMapper mailMapper;

    public MailModel save(MailModel mailModel) {
        Mail mail = mailMapper.map(mailModel);
        mail.setTimestamp(OffsetDateTime.now());
        return mailMapper.map(mailRepository.save(mail));
    }
}