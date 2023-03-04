package com.system.libsystem.entities.passwordremindertoken;

import com.system.libsystem.entities.user.UserEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class PasswordReminderTokenEntity {

    @Id
    @SequenceGenerator(name = "password_reminder_token_id_seq", sequenceName = "password_reminder_token_id_seq")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "password_reminder_token_id_seq")
    private Long id;

    @Column(nullable = false)
    private String token;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private UserEntity userEntity;

    @Column(nullable = false)
    private Date expiryDate;

}
