package com.system.libsystem.rest.registration.mail;

public interface MailSender {

    void send(String to, String mail, String subject);

}
