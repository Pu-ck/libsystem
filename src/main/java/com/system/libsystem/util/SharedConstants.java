package com.system.libsystem.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SharedConstants {
    public static final String FIND_USER_EXCEPTION_LOG = "Unable to find user ";
    public static final String FIND_BOOK_EXCEPTION_LOG = "Unable to find book ";
    public static final String FIND_BORROWED_BOOK_EXCEPTION_LOG = "Unable to find borrowed book ";
    public static final String INVALID_CARD_NUMBER_LOG = "Invalid card number";
    public static final String AFFILIATE_A = "Affiliate A";
    public static final String AFFILIATE_B = "Affiliate B";

}
