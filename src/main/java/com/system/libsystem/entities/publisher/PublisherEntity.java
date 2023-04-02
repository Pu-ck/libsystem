package com.system.libsystem.entities.publisher;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class PublisherEntity {

    @Id
    @SequenceGenerator(name = "publisher_id_seq", sequenceName = "publisher_id_seq")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "publisher_id_seq")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String address;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

}
