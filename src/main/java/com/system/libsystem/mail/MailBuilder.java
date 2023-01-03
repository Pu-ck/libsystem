package com.system.libsystem.mail;

import org.springframework.stereotype.Component;

@Component
public class MailBuilder {
    public String getAccountConfirmationMailBody(String userName, String firstName, String lastName, String cardNumber,
                                                  String registrationTime, String confirmationAddress) {
        return "New account registration request, " +
                "<p>First name: " + firstName + "</p>" +
                "<p>Last name: " + lastName + "</p>" +
                "<p>Card number: " + cardNumber + "</p>" +
                "<p>Username (email): " + userName + "</p>" +
                "<p>Registration time: " + registrationTime + "</p>" +
                "<p><a href=\"" + confirmationAddress + "\">Confirm and enable requested account in system</a></p>";
    }
    public String getAccountEnabledMailBody(String firstName, String lastName) {
        return "Hello " + firstName + " " + lastName + "," +
                "<p>Your account has been verified, accepted and enabled by the administrator, you can now " +
                "login to the library system.</p>";
    }

    public String getAccountRegisteredMailBody(String userName, String firstName, String lastName, String cardNumber) {
        return "Hello " + firstName + " " + lastName + "," +
                "<p>Your library system account has been created:</p>" +
                "<p>Username (email): " + userName + "</p>" +
                "<p>Card number: " + cardNumber + "</p>" +
                "<p>Wait until the administrator enables your account. You will receive a confirmation mail " +
                "when your new account will be enabled.</p>";
    }
}
