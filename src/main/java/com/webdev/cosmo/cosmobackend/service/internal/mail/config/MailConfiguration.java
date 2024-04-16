package com.webdev.cosmo.cosmobackend.service.internal.mail.config;

import com.webdev.cosmo.cosmobackend.service.internal.mail.mapper.MailMapper;
import com.webdev.cosmo.cosmobackend.service.internal.mail.repository.MailRepository;
import com.webdev.cosmo.cosmobackend.service.internal.mail.service.MailExistanceValidator;
import com.webdev.cosmo.cosmobackend.service.internal.mail.service.MailSaveService;
import com.webdev.cosmo.cosmobackend.service.internal.mail.service.MailSenderConsumer;
import com.webdev.cosmo.cosmobackend.util.templates.SaveService;
import com.webdev.cosmo.cosmobackend.util.templates.Validator;
import org.openapitools.model.MailModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Properties;

@Configuration
public class MailConfiguration {

    @Bean
    public JavaMailSender getJavaMailSender(
            final MailProperties mailProperties
    ) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());

        mailSender.setUsername(mailProperties.getUsername());
        mailSender.setPassword(mailProperties.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", mailProperties.getProtocol());
        props.put("mail.smtp.auth", mailProperties.getAuth());
        props.put("mail.smtp.starttls.enable", mailProperties.getStarttlsEnable());
        props.put("mail.debug", mailProperties.getDebug());

        return mailSender;
    }

    @Bean
    public TemplateEngine customTemplateEngine() {
        TemplateEngine templateEngine = new SpringTemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    @Bean
    public MailSenderConsumer emailService(
            final JavaMailSender javaMailSender,
            final TemplateEngine customTemplateEngine,
            final SaveService<MailModel, MailModel> mailSaveService,
            final MailMapper mailMapper
    ) {
        return new MailSenderConsumer(javaMailSender, customTemplateEngine, mailSaveService, mailMapper);
    }

    @Bean
    public SaveService<MailModel, MailModel> mailSaveService(
            final MailRepository mailRepository,
            final MailMapper mailMapper,
            final Validator<MailModel> mailExistanceValidator
            ) {
        return new MailSaveService(mailRepository, mailExistanceValidator, mailMapper);
    }

    @Bean
    public Validator<MailModel> mailExistanceValidator(
            final MailRepository mailRepository
    ) {
        return new MailExistanceValidator(mailRepository);
    }
}
