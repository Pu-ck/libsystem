package com.system.libsystem.rest.administration.cardnumber;

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

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class RegisterNewCardNumberService {

    private static final int PESEL_NUMBER_LENGTH = 11;
    private static final int CARD_NUMBER_LENGTH = 10;
    private final CardNumberRepository cardNumberRepository;

    public void registerNewCardNumber(RegisterNewCardNumberRequest registerNewCardNumberRequest) {
        validateCardNumberAndPeselNumberFormats(registerNewCardNumberRequest);
        checkIfCardNumberIsAlreadyRegistered(registerNewCardNumberRequest);

        CardNumberEntity cardNumberEntity = new CardNumberEntity();
        final String hashedPeselNumber = Optional.ofNullable(HashingUtil.hashData(registerNewCardNumberRequest
                .getPeselNumber())).orElseThrow(() -> new HashingException("Unable to hash the PESEL number"));

        cardNumberEntity.setCardNumber(registerNewCardNumberRequest.getCardNumber());
        cardNumberEntity.setPeselNumber(hashedPeselNumber);
        cardNumberRepository.save(cardNumberEntity);
        log.error("New card number " + registerNewCardNumberRequest.getCardNumber() + " registered");
    }

    private void validateCardNumberAndPeselNumberFormats(RegisterNewCardNumberRequest registerNewCardNumberRequest) {
        if (registerNewCardNumberRequest.getCardNumber().toString().length() != CARD_NUMBER_LENGTH) {
            throw new InvalidCardNumberFormatException();
        }
        if (registerNewCardNumberRequest.getPeselNumber().length() != PESEL_NUMBER_LENGTH) {
            throw new InvalidPeselNumberFormatException();
        }
    }

    private void checkIfCardNumberIsAlreadyRegistered(RegisterNewCardNumberRequest registerNewCardNumberRequest) {
        final List<CardNumberEntity> cardNumberEntities = cardNumberRepository.findAll();
        if (cardNumberEntities.stream().anyMatch(cardNumberEntity -> HashingUtil
                .compareRawAndHashedData(registerNewCardNumberRequest.getPeselNumber(), cardNumberEntity.getPeselNumber()))) {
            throw new PeselNumberAlreadyTakenException();
        }
        if (cardNumberRepository.findByCardNumber(registerNewCardNumberRequest.getCardNumber()).isPresent()) {
            throw new CardNumberAlreadyTakenException();
        }
    }

}
