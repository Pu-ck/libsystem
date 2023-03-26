package com.system.libsystem.entities.genre;

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
public class GenreEntity {

    @Id
    @SequenceGenerator(name = "genre_id_seq", sequenceName = "genre_id_seq")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "genre_id_seq")
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "genres")
    @JsonBackReference("book-genres")
    private Set<BookEntity> books = new HashSet<>();

}
