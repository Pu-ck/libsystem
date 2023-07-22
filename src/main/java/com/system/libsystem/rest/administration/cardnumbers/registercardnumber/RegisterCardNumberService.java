package com.system.libsystem.rest.administration.cardnumbers.registercardnumber;

import com.system.libsystem.entities.cardnumber.CardNumberEntity;
import com.system.libsystem.entities.cardnumber.CardNumberRepository;
import com.system.libsystem.exceptions.cardnumber.CardNumberAlreadyTakenException;
import com.system.libsystem.exceptions.cardnumber.InvalidCardNumberFormatException;
import com.system.libsystem.exceptions.hashing.HashingException;
import com.system.libsystem.exceptions.peselnumber.InvalidPeselNumberFormatException;
import com.system.libsystem.exceptions.peselnumber.PeselNumberAlreadyTakenException;
import com.system.libsystem.util.HashingUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class RegisterCardNumberService {

    private static final int PESEL_NUMBER_LENGTH = 11;
    private static final int CARD_NUMBER_LENGTH = 10;

    private final CardNumberRepository cardNumberRepository;

    public void registerNewCardNumber(RegisterCardNumberRequest registerCardNumberRequest) {
        validateCardNumberAndPeselNumberFormats(registerCardNumberRequest);
        checkIfCardNumberIsAlreadyRegistered(registerCardNumberRequest);

        final CardNumberEntity cardNumberEntity = new CardNumberEntity();
        final String hashedPeselNumber = Optional.ofNullable(HashingUtil.hashData(registerCardNumberRequest
                .getPeselNumber())).orElseThrow(() -> new HashingException("Unable to hash the PESEL number"));

        cardNumberEntity.setCardNumber(registerCardNumberRequest.getCardNumber());
        cardNumberEntity.setPeselNumber(hashedPeselNumber);
        cardNumberRepository.save(cardNumberEntity);
        log.error("New card number " + registerCardNumberRequest.getCardNumber() + " registered");
    }

    private void validateCardNumberAndPeselNumberFormats(RegisterCardNumberRequest registerCardNumberRequest) {
        if (registerCardNumberRequest.getCardNumber().toString().length() != CARD_NUMBER_LENGTH) {
            throw new InvalidCardNumberFormatException();
        }
        if (registerCardNumberRequest.getPeselNumber().length() != PESEL_NUMBER_LENGTH) {
            throw new InvalidPeselNumberFormatException();
        }
    }

    private void checkIfCardNumberIsAlreadyRegistered(RegisterCardNumberRequest registerCardNumberRequest) {
        final List<CardNumberEntity> cardNumberEntities = cardNumberRepository.findAll();
        if (cardNumberEntities.stream().anyMatch(cardNumberEntity -> HashingUtil
                .compareRawAndHashedData(registerCardNumberRequest.getPeselNumber(), cardNumberEntity.getPeselNumber()))) {
            throw new PeselNumberAlreadyTakenException();
        }
        if (cardNumberRepository.findByCardNumber(registerCardNumberRequest.getCardNumber()).isPresent()) {
            throw new CardNumberAlreadyTakenException();
        }
    }

}
