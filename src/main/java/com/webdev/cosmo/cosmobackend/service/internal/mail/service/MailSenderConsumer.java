package com.webdev.cosmo.cosmobackend.service.internal.mail.service;

import com.webdev.cosmo.cosmobackend.service.api.Mail;
import com.webdev.cosmo.cosmobackend.service.internal.mail.MailContext;
import com.webdev.cosmo.cosmobackend.service.internal.mail.mapper.MailMapper;
import com.webdev.cosmo.cosmobackend.util.templates.SaveService;
import com.webdev.cosmo.cosmobackend.util.util.BetterOptional;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.openapitools.model.MailModel;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class MailSenderConsumer implements Consumer<MailContext> {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final SaveService<MailModel, MailModel> mailSaveService;
    private final MailMapper mailMapper;

    @Override
    @SneakyThrows
    public void accept(MailContext mailContext) {
        String body = templateEngine.process(mailContext.getSubject(), buildContext(mailContext.getArgs()));
        MimeMessage message = mailSender.createMimeMessage();
        buildMessageHelper(message, mailContext.getRecipient(), mailContext.getSubject(), body);

        mailSender.send(message);
        mailSaveService.save(mailMapper.map(mailContext));
    }

    private void buildMessageHelper(MimeMessage message, String mailContext, String mailContext1, String body) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(mailContext);
        helper.setSubject(mailContext1);
        helper.setText(body, true);
    }

    private Context buildContext(Map<String, Object> variables) {
        return BetterOptional.of(new Context())
                .peek(c -> c.setVariables(variables))
                .get();
    }
}
