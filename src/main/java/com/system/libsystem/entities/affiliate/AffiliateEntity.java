package com.system.libsystem.entities.affiliate;

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
public class AffiliateEntity {

    @Id
    @SequenceGenerator(name = "affiliate_id_seq", sequenceName = "affiliate_id_seq")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "affiliate_id_seq")
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String address;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @ManyToMany(mappedBy = "affiliates")
    @JsonBackReference("book-affiliates")
    private Set<BookEntity> books = new HashSet<>();

}
