package com.system.libsystem.rest.util;

import com.system.libsystem.entities.book.BookEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.system.libsystem.util.SharedConstants.AFFILIATE_A;
import static com.system.libsystem.util.SharedConstants.AFFILIATE_B;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
@Slf4j
public final class BookUtil {

    public void setCurrentQuantityInAffiliate(BookEntity bookEntity, String affiliate, int changeQuantityValue) {
        if (affiliate.equals(AFFILIATE_A)) {
            bookEntity.setCurrentQuantityAffiliateA(bookEntity.getCurrentQuantityAffiliateA() + changeQuantityValue);
            log.info("The current quantity in Affiliate A for book with id " + bookEntity.getId() +
                    " has been changed by " + changeQuantityValue);
        } else if (affiliate.equals(AFFILIATE_B)) {
            bookEntity.setCurrentQuantityAffiliateB(bookEntity.getCurrentQuantityAffiliateB() + changeQuantityValue);
            log.info("The current quantity in Affiliate B for book with id " + bookEntity.getId() +
                    " has been changed by " + changeQuantityValue);
        }
    }

    public boolean isCardNumberValid(long userCardNumber, long confirmOrderRequestCardNumber) {
        return Objects.equals(userCardNumber, confirmOrderRequestCardNumber);
    }

}
