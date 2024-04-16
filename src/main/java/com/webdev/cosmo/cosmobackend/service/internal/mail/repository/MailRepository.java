package com.webdev.cosmo.cosmobackend.service.internal.mail.repository;

import com.webdev.cosmo.cosmobackend.service.api.Mail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailRepository extends JpaRepository<Mail, String> {
}
