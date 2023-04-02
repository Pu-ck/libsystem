package com.system.libsystem.entities.affiliatebook;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "book_affiliate")
@IdClass(AffiliateBookId.class)
public class AffiliateBook implements Serializable  {

    @Id
    @Column(name = "affiliate_id")
    private int affiliateId;

    @Id
    @Column(name = "book_id")
    private int bookId;

    @Column(name = "currentQuantity")
    private int currentQuantity;

    @Column(name = "generalQuantity")
    private int generalQuantity;

}
