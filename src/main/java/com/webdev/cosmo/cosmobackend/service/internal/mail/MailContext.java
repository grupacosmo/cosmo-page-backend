package com.webdev.cosmo.cosmobackend.service.internal.mail;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.Objects;

@Data
@Accessors(chain = true)
public class MailContext {
    private String recipient;
    private String subject;
    private String templateName;
    private Map<String, Object> args;
}
