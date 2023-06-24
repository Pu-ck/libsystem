package com.system.libsystem.rest.util;

import com.system.libsystem.entities.affiliatebook.AffiliateBook;
import com.system.libsystem.entities.affiliatebook.AffiliateBookRepository;
import com.system.libsystem.entities.affiliate.AffiliateEntity;
import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.exceptions.affiliate.AffiliateBookNotFoundException;
import com.system.libsystem.exceptions.affiliate.AffiliateNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Component
@Slf4j
public final class BookUtil {

    private final AffiliateBookRepository affiliateBookRepository;

    public void setCurrentQuantityInAffiliate(BookEntity bookEntity, String affiliate, int changeQuantityValue) {
        final AffiliateEntity affiliateEntity = bookEntity.getAffiliates().stream()
                .filter(searchedAffiliate -> searchedAffiliate.getName().equals(affiliate))
                .findFirst()
                .orElseThrow(() -> new AffiliateNotFoundException(affiliate));
        final AffiliateBook affiliateBook = affiliateBookRepository.findByBookIdAndAffiliateId(bookEntity.getId(),
                affiliateEntity.getId()).orElseThrow(() ->
                new AffiliateBookNotFoundException(affiliate, bookEntity.getId()));

        affiliateBook.setCurrentQuantity(affiliateBook.getCurrentQuantity() + changeQuantityValue);
        affiliateBookRepository.save(affiliateBook);

        log.info("The current quantity in " + affiliate + " for book with id " + bookEntity.getId() +
                " has been changed by " + changeQuantityValue);
    }

    public boolean isCardNumberValid(long userCardNumber, long confirmOrderRequestCardNumber) {
        return Objects.equals(userCardNumber, confirmOrderRequestCardNumber);
    }

}
