package com.system.libsystem.entities.book;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class BookEntity {

    @Id
    @SequenceGenerator(name = "book_id_seq", sequenceName = "book_id_seq")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "book_id_seq")
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private String yearOfPrint;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int currentQuantityAffiliateA;

    @Column(nullable = false)
    private int generalQuantityAffiliateA;

    @Column(nullable = false)
    private int currentQuantityAffiliateB;

    @Column(nullable = false)
    private int generalQuantityAffiliateB;

}
