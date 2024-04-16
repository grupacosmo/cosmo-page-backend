package com.webdev.cosmo.cosmobackend.service.internal.mail.service;

import com.webdev.cosmo.cosmobackend.service.internal.mail.repository.MailRepository;
import com.webdev.cosmo.cosmobackend.util.templates.Validator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.MailModel;

@RequiredArgsConstructor
public class MailExistanceValidator implements Validator<MailModel> {
    private final MailRepository mailRepository;

    @Override
    public boolean exists(MailModel mailModel) {
        return mailRepository.existsById(mailModel.getId());
    }
}
