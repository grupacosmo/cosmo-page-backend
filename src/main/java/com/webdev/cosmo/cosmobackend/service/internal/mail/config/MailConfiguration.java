package com.webdev.cosmo.cosmobackend.service.internal.mail.config;

import com.webdev.cosmo.cosmobackend.service.internal.mail.mapper.MailMapper;
import com.webdev.cosmo.cosmobackend.service.internal.mail.repository.MailRepository;
import com.webdev.cosmo.cosmobackend.service.internal.mail.service.MailSaveService;
import com.webdev.cosmo.cosmobackend.util.interfaces.SaveService;
import org.openapitools.model.MailModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailConfiguration {

    @Bean
    public SaveService<MailModel, MailModel> mailSaveService(
            final MailRepository mailRepository,
            final MailMapper mailMapper
            ) {
        return new MailSaveService(mailRepository, mailMapper);
    }
}