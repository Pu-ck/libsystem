package com.system.libsystem.rest.administration.cardnumber;

import com.system.libsystem.entities.cardnumber.CardNumberEntity;
import com.system.libsystem.entities.cardnumber.CardNumberRepository;
import com.system.libsystem.exceptions.cardnumber.CardNumberAlreadyTakenException;
import com.system.libsystem.exceptions.cardnumber.InvalidCardNumberFormatException;
import com.system.libsystem.exceptions.peselnumber.InvalidPeselNumberFormatException;
import com.system.libsystem.exceptions.peselnumber.PeselNumberAlreadyTakenException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class RegisterNewCardNumberService {

    private static final int PESEL_NUMBER_LENGTH = 11;
    private static final int CARD_NUMBER_LENGTH = 10;
    private final CardNumberRepository cardNumberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void registerNewCardNumber(RegisterNewCardNumberRequest registerNewCardNumberRequest) {
        validateCardNumberAndPeselNumberFormats(registerNewCardNumberRequest);
        checkIfCardNumberIsAlreadyRegistered(registerNewCardNumberRequest);
        CardNumberEntity cardNumberEntity = new CardNumberEntity();
        cardNumberEntity.setCardNumber(registerNewCardNumberRequest.getCardNumber());
        cardNumberEntity.setPeselNumber(bCryptPasswordEncoder.encode(registerNewCardNumberRequest.getPeselNumber()));
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
        if (cardNumberEntities.stream().anyMatch(cardNumberEntity -> bCryptPasswordEncoder
                .matches(registerNewCardNumberRequest.getPeselNumber(), cardNumberEntity.getPeselNumber()))) {
            throw new PeselNumberAlreadyTakenException();
        }
        if (cardNumberRepository.findByCardNumber(registerNewCardNumberRequest.getCardNumber()).isPresent()) {
            throw new CardNumberAlreadyTakenException();
        }
    }

}
