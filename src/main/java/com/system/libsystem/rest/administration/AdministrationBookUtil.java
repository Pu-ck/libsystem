package com.system.libsystem.rest.administration;

import com.system.libsystem.entities.book.BookEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import static com.system.libsystem.util.SharedConstants.AFFILIATE_A;
import static com.system.libsystem.util.SharedConstants.AFFILIATE_B;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public final class AdministrationBookUtil {

    public void setCurrentQuantityInAffiliate(BookEntity bookEntity, String affiliate, int changeQuantityValue) {
        if (affiliate.equals(AFFILIATE_A)) {
            bookEntity.setCurrentQuantityAffiliateA(bookEntity.getCurrentQuantityAffiliateA() + changeQuantityValue);
        } else if (affiliate.equals(AFFILIATE_B)) {
            bookEntity.setCurrentQuantityAffiliateB(bookEntity.getCurrentQuantityAffiliateB() + changeQuantityValue);
        }
    }

}
