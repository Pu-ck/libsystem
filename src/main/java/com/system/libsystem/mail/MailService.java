package com.system.libsystem.mail;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailService implements MailSender {

    private static final String ENCODING_FORMAT = "utf-8";
    private final JavaMailSender javaMailSender;
    @Value("${mail.sender}")
    private String sender;

    @Override
    @Async
    public void send(String to, String mail, String subject) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, ENCODING_FORMAT);
            mimeMessageHelper.setText(mail, true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom(sender);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException exception) {
            throw new IllegalStateException("Mail sending failure " + exception.getMessage());
        }

    }

}
