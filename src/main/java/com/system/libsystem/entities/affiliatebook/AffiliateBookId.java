package com.system.libsystem.entities.affiliatebook;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AffiliateBookId implements Serializable {

    private Long affiliateId;
    private Long bookId;

}
