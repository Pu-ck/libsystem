package com.system.libsystem.entities.book;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.system.libsystem.entities.affiliate.AffiliateEntity;
import com.system.libsystem.entities.affiliatebook.AffiliateBook;
import com.system.libsystem.entities.author.AuthorEntity;
import com.system.libsystem.entities.genre.GenreEntity;
import com.system.libsystem.entities.publisher.PublisherEntity;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.yearofprint.YearOfPrintEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
public class BookEntity {

    @Id
    @SequenceGenerator(name = "book_id_seq", sequenceName = "book_id_seq")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "book_id_seq")
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToMany(mappedBy = "favouriteBooks")
    @JsonBackReference("user-books-favourites")
    private Set<UserEntity> users = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @JsonManagedReference("book-authors")
    private Set<AuthorEntity> authors = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "book_genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @JsonManagedReference("book-genres")
    private Set<GenreEntity> genres = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "book_affiliate",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "affiliate_id")
    )
    @JsonManagedReference("book-affiliates")
    private Set<AffiliateEntity> affiliates = new HashSet<>();

    @OneToMany(mappedBy = "bookId")
    @JsonManagedReference("book-affiliatebook")
    private Set<AffiliateBook> affiliateBooks = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private PublisherEntity publisherEntity;

    @ManyToOne
    @JoinColumn(name = "year_of_print_id")
    private YearOfPrintEntity yearOfPrintEntity;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Date addDate;

    @JsonIgnore
    public String getFormattedGenresAsString() {
        return this.getGenres().stream().map(GenreEntity::getName).collect(Collectors.joining(", "));
    }

    public String getFormattedAuthorsAsString() {
        return this.getAuthors().stream().map(AuthorEntity::getName).collect(Collectors.joining(", "));
    }

}
