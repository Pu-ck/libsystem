package com.system.libsystem.entities.borrowedbook;

import com.system.libsystem.entities.affiliate.AffiliateEntity;
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
    private Long id;

    @Column(nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long cardNumber;

    @Column
    private Date borrowDate;

    @Column
    private Date returnDate;

    @Column
    private Date readyDate;

    @Column(nullable = false)
    private BigDecimal penalty;

    @ManyToOne
    @JoinColumn(name = "affiliate_id")
    private AffiliateEntity affiliateEntity;

    @Column(nullable = false)
    private boolean accepted;

    @Column(nullable = false)
    private boolean extended;

    @Column(nullable = false)
    private boolean closed;

    @Column(nullable = false)
    private boolean ready;

}
