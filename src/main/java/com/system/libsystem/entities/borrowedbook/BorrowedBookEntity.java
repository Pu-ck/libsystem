package com.system.libsystem.entities.borrowedbook;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Getter
@Setter
public class BorrowedBookEntity {

    @Id
    @SequenceGenerator(name = "borrowed_book_id_seq", sequenceName = "borrowed_book_id_seq")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "borrowed_book_id_seq")
    private int id;

    @Column(nullable = false)
    private int bookId;

    @Column(nullable = false)
    private int userId;

    @Column(nullable = false)
    private Long cardNumber;

    @Column
    private Date borrowDate;

    @Column
    private Date returnDate;

    @Column(nullable = false)
    private BigDecimal penalty;

    @Column(nullable = false)
    private String affiliate;

    @Column(nullable = false)
    private boolean accepted;

    @Column(nullable = false)
    private boolean extended;

}
