package com.system.libsystem.entities.confirmationtoken;

import com.system.libsystem.entities.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationTokenEntity {

    public ConfirmationTokenEntity(String token, UserEntity userEntity) {
        this.token = token;
        this.userEntity = userEntity;
    }

    @Id
    @SequenceGenerator(name = "confirmation_token_id_seq", sequenceName = "confirmation_token_id_seq")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "confirmation_token_id_seq")
    private Long id;

    @Column(nullable = false)
    private String token;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private UserEntity userEntity;

}
