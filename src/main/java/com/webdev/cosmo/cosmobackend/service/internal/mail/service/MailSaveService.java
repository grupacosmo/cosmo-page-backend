package com.webdev.cosmo.cosmobackend.service.internal.mail.service;

import com.webdev.cosmo.cosmobackend.service.api.Mail;
import com.webdev.cosmo.cosmobackend.service.internal.mail.mapper.MailMapper;
import com.webdev.cosmo.cosmobackend.service.internal.mail.repository.MailRepository;
import com.webdev.cosmo.cosmobackend.util.interfaces.SaveService;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.MailModel;

import java.time.OffsetDateTime;

@RequiredArgsConstructor
public class MailSaveService implements SaveService<MailModel, MailModel> {
    private final MailRepository mailRepository;
    private final MailMapper mailMapper;

    @Override
    public MailModel save(MailModel mailModel) {
        Mail mail = mailMapper.map(mailModel);
        mail.setTimestamp(OffsetDateTime.now());
        return mailMapper.map(mailRepository.save(mail));
    }
}