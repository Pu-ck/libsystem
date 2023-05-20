package com.system.libsystem.entities.cardnumber;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class CardNumberEntity {

    @Id
    @SequenceGenerator(name = "card_number_id_seq", sequenceName = "card_number_id_seq")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "card_number_id_seq")
    private int id;

    @Column(nullable = false, unique = true)
    private Long cardNumber;

    @Column(nullable = false, unique = true)
    private String peselNumber;

}
