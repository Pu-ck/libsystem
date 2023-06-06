package com.system.libsystem.entities.newbook;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class NewBookEntity {

    @Id
    @SequenceGenerator(name = "new_book_id_seq", sequenceName = "new_book_id_seq")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "new_book_id_seq")
    private Long id;

    @Column(nullable = false, unique = true)
    private Long bookId;

}
