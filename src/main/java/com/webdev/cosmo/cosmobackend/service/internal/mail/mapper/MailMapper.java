package com.webdev.cosmo.cosmobackend.service.internal.mail.mapper;

import com.webdev.cosmo.cosmobackend.service.api.Mail;
import com.webdev.cosmo.cosmobackend.service.internal.mail.MailContext;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.openapitools.model.MailModel;

import java.time.OffsetDateTime;

@Mapper
public interface MailMapper {
    Mail map(MailModel mailModel);
    MailModel map(Mail mail);

    @Mapping(source = "recipient", target = "recipientEmail")
    MailModel map(MailContext mailContext);
}
