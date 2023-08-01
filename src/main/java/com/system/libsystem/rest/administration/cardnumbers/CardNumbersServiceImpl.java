package com.system.libsystem.rest.administration.cardnumbers;

import com.system.libsystem.entities.cardnumber.CardNumberEntity;
import com.system.libsystem.entities.cardnumber.CardNumberRepository;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserRepository;
import com.system.libsystem.exceptions.cardnumber.CardNumberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardNumbersServiceImpl implements CardNumberService {

    private final CardNumberRepository cardNumberRepository;
    private final UserRepository userRepository;

    @Override
    public List<CardNumberDTO> getCardNumbers() {
        return getCardNumberDTOs();
    }

    @Override
    public List<CardNumberDTO> getCardNumberById(Long cardNumberId) {
        if (cardNumberId == null) {
            return getCardNumbers();
        }
        return getCardNumberDTOById(cardNumberId);
    }

    @Override
    public List<CardNumberDTO> getCardNumberByCardNumber(Long cardNumber) {
        if (cardNumber == null) {
            return getCardNumbers();
        }
        return getCardNumberDTOByCardNumber(cardNumber);
    }

    private List<CardNumberDTO> getCardNumberDTOs() {
        final List<CardNumberDTO> cardNumberDTOs = new ArrayList<>();
        for (CardNumberEntity cardNumberEntity : cardNumberRepository.findAll()) {
            cardNumberDTOs.add(getCardNumberDTO(cardNumberEntity));
        }
        return cardNumberDTOs;
    }

    private List<CardNumberDTO> getCardNumberDTOById(Long cardNumberId) {
        final List<CardNumberDTO> cardNumberDTOs = new ArrayList<>();
        final CardNumberEntity cardNumberEntity = cardNumberRepository.findById(cardNumberId).orElseThrow(() ->
                new CardNumberNotFoundException(cardNumberId));
        cardNumberDTOs.add(getCardNumberDTO(cardNumberEntity));
        return cardNumberDTOs;
    }

    private List<CardNumberDTO> getCardNumberDTOByCardNumber(Long cardNumber) {
        final List<CardNumberDTO> cardNumberDTOs = new ArrayList<>();
        final CardNumberEntity cardNumberEntity = cardNumberRepository.findByCardNumber(cardNumber).orElseThrow(() ->
                new CardNumberNotFoundException(cardNumber));
        cardNumberDTOs.add(getCardNumberDTO(cardNumberEntity));
        return cardNumberDTOs;
    }

    private CardNumberDTO getCardNumberDTO(CardNumberEntity cardNumberEntity) {
        final CardNumberDTO cardNumberDTO = new CardNumberDTO();
        cardNumberDTO.setId(cardNumberEntity.getId());
        cardNumberDTO.setCardNumber(cardNumberEntity.getCardNumber());
        cardNumberDTO.setUserId(getIdOfUserAssociatedWithCardNumber(cardNumberEntity));
        return cardNumberDTO;
    }

    private Long getIdOfUserAssociatedWithCardNumber(CardNumberEntity cardNumberEntity) {
        UserEntity userEntity = userRepository.findByCardNumber(cardNumberEntity.getCardNumber()).orElse(null);
        if (userEntity == null) {
            return null;
        }
        return userEntity.getId();
    }
}
