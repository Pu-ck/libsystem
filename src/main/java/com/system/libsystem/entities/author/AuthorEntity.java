package com.system.libsystem.entities.author;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.system.libsystem.entities.book.BookEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class AuthorEntity {

    @Id
    @SequenceGenerator(name = "author_id_seq", sequenceName = "author_id_seq")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "author_id_seq")
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "authors")
    @JsonBackReference("book-authors")
    private Set<BookEntity> books = new HashSet<>();

}
