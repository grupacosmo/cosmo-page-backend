package com.webdev.cosmo.cosmobackend.service.internal.mail.config;

import com.webdev.cosmo.cosmobackend.service.internal.mail.mapper.MailMapper;
import com.webdev.cosmo.cosmobackend.service.internal.mail.repository.MailRepository;
import com.webdev.cosmo.cosmobackend.service.internal.mail.service.MailSaveService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailConfiguration {

    @Bean
    public MailSaveService mailSaveService(
            final MailRepository mailRepository,
            final MailMapper mailMapper
            ) {
        return new MailSaveService(mailRepository, mailMapper);
    }
}