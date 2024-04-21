package com.webdev.cosmo.cosmobackend.service.internal.mail.service;

import com.webdev.cosmo.cosmobackend.service.internal.mail.mapper.MailMapper;
import com.webdev.cosmo.cosmobackend.service.internal.mail.repository.MailRepository;
import com.webdev.cosmo.cosmobackend.util.interfaces.SaveService;
import com.webdev.cosmo.cosmobackend.util.BetterOptional;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.MailModel;

import java.time.OffsetDateTime;

import static com.webdev.cosmo.cosmobackend.error.Error.MAIL_SAVE_ERROR;

@RequiredArgsConstructor
public class MailSaveService implements SaveService<MailModel, MailModel> {
    private final MailRepository mailRepository;
    private final MailMapper mailMapper;

    @Override
    public MailModel save(MailModel mailModel) {
        return BetterOptional.of(mailModel)
                .map(mailMapper::map)
                .peek(mail -> mail.setTimestamp(OffsetDateTime.now()))
                .map(mailRepository::save)
                .map(mailMapper::map)
                .orElseThrow(MAIL_SAVE_ERROR::getError);
    }

}
