package com.system.libsystem.mail;

public interface MailSender {
    void send(String to, String mail, String subject);
}
