package com.system.libsystem.mail;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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

    public String getNewPasswordSetMailBody(String firstName, String lastName, String loginPageAddress) {
        return "Hello " + firstName + " " + lastName + "," +
                "<p>Your new password has been successfully set. You can now you can now " +
                "<a href=\"" + loginPageAddress + "\">login</a> to the library system.</p>";
    }

    public String getBookBorrowMailBody(String firstName, String lastName, String title, String author,
                                        int orderNumber, String affiliate) {
        return "Hello " + firstName + " " + lastName + "," +
                "<p>You have ordered a new book from library affiliate: " + affiliate + "<p>" +
                "<p>Title: " + title + "<p>" +
                "<p>Author: " + author + "<p>" +
                "<p>Order number: " + orderNumber + "<p>" +
                "<p>You can pick up your order at your local library.</p>" +
                "<p>After picking up order books you will be able to check all your borrowed books, including " +
                "estimated return date and possible penalty, in your user profile information.</p>";
    }

    public String getBookReturnDateExtensionRequestMailBody(int userId, String cardNumber, int borrowedBookId,
                                                            String borrowDate, String returnDate, String affiliate,
                                                            BigDecimal penalty) {
        return "New book return date extension request, " +
                "<p>User id: " + userId + "</p>" +
                "<p>Card number: " + cardNumber + "</p>" +
                "<p>Borrowed book id: " + borrowedBookId + "</p>" +
                "<p>Borrow date: " + borrowDate + "</p>" +
                "<p>Return date: " + returnDate + "</p>" +
                "<p>Current penalty: " + penalty + "</p>" +
                "<p>Library affiliate: " + affiliate + "</p>" +
                "<p>Accept the request in the library system.</p>";
    }

    public String getBookReturnDateExtensionConfirmationMailBody(String firstName, String lastName, String title,
                                                                 String author, String newReturnDate, String affiliate) {
        return "Hello " + firstName + " " + lastName + "," +
                "<p>Your book's return date extension request has been accepted. Extended book's details: <p>" +
                "<p>Title: " + title + "<p>" +
                "<p>Author: " + author + "<p>" +
                "<p>Library affiliate: " + affiliate + "<p>" +
                "<p>New return date: " + newReturnDate + "<p>";
    }

    public String getOrderedBookPenaltyInformationMailBody(String firstName, String lastName, String title,
                                                           String author, String returnDate, String affiliate,
                                                           BigDecimal penalty) {
        return "Hello " + firstName + " " + lastName + "," +
                "<p>The time to return a book you have ordered has expired and a penalty was increased. " +
                "Book's details: <p>" +
                "<p>Title: " + title + "<p>" +
                "<p>Author: " + author + "<p>" +
                "<p>Library affiliate: " + affiliate + "<p>" +
                "<p>Return date: " + returnDate + "<p>" +
                "<p>Current penalty: " + penalty + "<p>" +
                "<p>If available, extend the return date of your book, or return it to your local library affiliate.<p>";
    }

    public String getComingUpBorrowedBookReturnDateMailBody(String firstName, String lastName, String title,
                                                            String author, String returnDate, String affiliate,
                                                            long daysToReturnDate) {
        return "Hello " + firstName + " " + lastName + "," +
                "<p>Your ordered book's return date is coming. Book's details: <p>" +
                "<p>Title: " + title + "<p>" +
                "<p>Author: " + author + "<p>" +
                "<p>Library affiliate: " + affiliate + "<p>" +
                "<p>Return date: " + returnDate + "<p>" +
                "<p>Days to return date: " + daysToReturnDate + "<p>" +
                "<p>If available, extend the return date of your book, or return it to your local library affiliate.<p>";
    }

    public String getBorrowedBookOrderReadyMailBody(String firstName, String lastName, String title,
                                                    String author, String affiliate) {
        return "Hello " + firstName + " " + lastName + "," +
                "<p>Your ordered book has been prepared for pick up. Book's details: <p>" +
                "<p>Title: " + title + "<p>" +
                "<p>Author: " + author + "<p>" +
                "<p>Library affiliate: " + affiliate + "<p>" +
                "<p>Pick up your ordered book at your local library affiliate within 2 days, otherwise the order " +
                "will be cancelled.<p>";
    }

    public String getBorrowedBookOrderRejectedMailBody(String firstName, String lastName, String title,
                                                       String author, String affiliate) {
        return "Hello " + firstName + " " + lastName + "," +
                "<p>Your ordered book has been rejected. Book's details: <p>" +
                "<p>Title: " + title + "<p>" +
                "<p>Author: " + author + "<p>" +
                "<p>Library affiliate: " + affiliate + "<p>" +
                "<p>Contact the administration for more information about your order rejection<p>";
    }

    public String getBorrowedBookOrderPickUpTimeExpired(String firstName, String lastName, String title,
                                               String author, String affiliate) {
        return "Hello " + firstName + " " + lastName + "," +
                "<p>Your ordered book pick up time has expired. Book's details: <p>" +
                "<p>Title: " + title + "<p>" +
                "<p>Author: " + author + "<p>" +
                "<p>Library affiliate: " + affiliate + "<p>" +
                "<p>The ordered book has been returned to the library catalogue.<p>";
    }

}
