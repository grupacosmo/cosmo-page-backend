package com.webdev.cosmo.cosmobackend.service.internal.mail.mapper;

import com.webdev.cosmo.cosmobackend.service.api.Mail;
import org.mapstruct.Mapper;
import org.openapitools.model.MailModel;

@Mapper
public interface MailMapper {
    Mail map(MailModel mailModel);
    MailModel map(Mail mail);
}