package com.system.libsystem.rest.registration;

import com.system.libsystem.rest.registration.mail.MailSender;
import com.system.libsystem.rest.registration.token.ConfirmationTokenEntity;
import com.system.libsystem.rest.registration.token.ConfirmationTokenService;
import com.system.libsystem.user.UserEntity;
import com.system.libsystem.user.UserService;
import com.system.libsystem.user.UserType;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final MailSender mailSender;
    @Value("${server.confirmation.address}")
    private String userConfirmationAddress;
    @Value("${mail.admin}")
    private String receiver;

    public String register(RegistrationRequest request) {
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(request.getPassword());
        userEntity.setUsername(request.getUsername());
        userEntity.setCardNumber(request.getCardNumber());
        userEntity.setFirstName(request.getFirstName());
        userEntity.setLastName(request.getLastName());
        userEntity.setEnabled(false);
        userEntity.setUserType(UserType.USER);

        LocalDateTime datetime = LocalDateTime.now();
        String registrationTime = datetime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        String token = userService.registerUser(userEntity);
        String confirmationAddress = userConfirmationAddress + token;
        mailSender.send(receiver, getAccountConfirmationMailBody(
                request.getUsername(),
                request.getFirstName(),
                request.getLastName(),
                request.getCardNumber().toString(),
                registrationTime,
                confirmationAddress), "New account registration request");

        return token;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationTokenEntity confirmationTokenEntity = confirmationTokenService.getToken(token).orElseThrow(() ->
                        new IllegalStateException("Confirmation token not found"));

        userService.enableUser(
                confirmationTokenEntity.getUserEntity().getUsername());

        mailSender.send(confirmationTokenEntity.getUserEntity().getUsername(), getAccountEnabledMailBody(
                confirmationTokenEntity.getUserEntity().getFirstName(),
                confirmationTokenEntity.getUserEntity().getLastName())
                , "Account enabled");

        return "Requested user was confirmed and enabled";
    }

    private String getAccountConfirmationMailBody(String userName, String firstName, String lastName, String cardNumber,
                                                  String registrationTime, String confirmationAddress) {


        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"left\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:20px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Account confirmation request</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"left\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">New account registration request</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <p>Registration time: "
                            + registrationTime + "</p> <p>Username (email): " + userName + "</p> <p>First name: " + firstName + "</p> <p>Last name: " + lastName + "</p> <p>Library card number: " + cardNumber +
                            "</p> <p><a href=\"" + confirmationAddress + "\">Confirm and enable requested account in system</a> </p></blockquote>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

    private String getAccountEnabledMailBody(String firstName, String lastName) {
        String content;
        Path filePath = Path.of("");
        try {
            content = Files.readString(filePath);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read cnfig file " + e.getMessage());
        }
        return content;
    }

}
