package com.system.libsystem.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SharedConstants {
    public static final String INVALID_CARD_NUMBER_LOG = "Invalid card number";
    public static final String BOOK_ALREADY_RETURNED_LOG = "Already returned - borrowed book with id ";
    public static final String BOOK_ALREADY_ACCEPTED_LOG = "Already accepted - borrowed book with id ";
    public static final String BOOK_ALREADY_EXTENDED_LOG = "Already extended - borrowed book with id ";
    public static final String AFFILIATE_A = "Affiliate A";
    public static final String AFFILIATE_B = "Affiliate B";

}
