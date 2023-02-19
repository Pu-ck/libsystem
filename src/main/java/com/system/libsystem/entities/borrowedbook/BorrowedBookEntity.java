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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private int bookId;

    @Column(nullable = false)
    private int userId;

    @Column(nullable = false)
    private Date borrowDate;

    @Column(nullable = false)
    private Date returnDate;

    @Column(nullable = false)
    private BigDecimal penalty;

}
