package com.system.libsystem.mail;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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

    public String getAccountEnabledMailBody(String firstName, String lastName, String loginPageAddress) {
        return "Hello " + firstName + " " + lastName + "," +
                "<p>Your account has been verified, accepted and enabled by the administrator, you can now " +
                "<a href=\"" + loginPageAddress + "\">login</a> to the library system.</p>";
    }

    public String getAccountRegisteredMailBody(String userName, String firstName, String lastName, String cardNumber) {
        return "Hello " + firstName + " " + lastName + "," +
                "<p>Your library system account has been created:</p>" +
                "<p>Username (email): " + userName + "</p>" +
                "<p>Card number: " + cardNumber + "</p>" +
                "<p>Wait until the administrator enables your account. You will receive a confirmation mail " +
                "when your new account will be enabled.</p>";
    }

    public String getPasswordReminderMailBody(String setNewPasswordAddress, String firstName, String lastName,
                                              String cardNumber) {
        return "Hello " + firstName + " " + lastName + "," +
                "<p>You have requested a password reminder for an account associated with library card number: "
                + cardNumber + "</p>" +
                "<p>Click the link below in order to set a new password to you library system account:</p>" +
                "<p><a href=\"" + setNewPasswordAddress + "\">Set new password</a></p>";
    }

    public String getBookBorrowMailBody(String firstName, String lastName, String title, String author,
                                        int orderNumber, String affiliate) {
        return "Hello " + firstName + " " + lastName + "," +
                "<p>You have ordered a new book from library affiliate " + affiliate + "<p>" +
                "<p>Title: " + title + "<p>" +
                "<p>Author: " + author + "<p>" +
                "<p>Order number: " + orderNumber + "<p>" +
                "<p>You can pick up your order at your local library.</p>" +
                "<p>After picking up order books you will be able to check all your borrowed books, including " +
                "estimated return date and possible penalty, in your user profile information.</p>";
    }

    public String getBookReturnDateExtensionRequestMailBody(int userId, String cardNumber, int bookId,
                                                            String borrowDate, String returnDate, String affiliate,
                                                            BigDecimal penalty) {
        return "New book return date extension request, " +
                "<p>User id: " + userId + "</p>" +
                "<p>Card number: " + cardNumber + "</p>" +
                "<p>Book id: " + bookId + "</p>" +
                "<p>Borrow date: " + borrowDate + "</p>" +
                "<p>Return date: " + returnDate + "</p>" +
                "<p>Current penalty: " + penalty + "</p>" +
                "<p>Library affiliate: " + affiliate + "</p>" +
                "<p>Accept the request in the library system.</p>";
    }

    public String getBookReturnDateExtensionConfirmationMailBody(String firstName, String lastName, String title,
                                                                 String author, String newReturnDate, String affiliate) {
        return "Hello " + firstName + " " + lastName + "," +
                "<p>Your book's return date extension request has been accepted. Extended book details: <p>" +
                "<p>Title: " + title + "<p>" +
                "<p>Author: " + author + "<p>" +
                "<p>Library affiliate: " + affiliate + "<p>" +
                "<p>New return date: " + newReturnDate + "<p>";
    }

}
