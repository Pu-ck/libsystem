package com.system.libsystem.rest.administration.cardnumbers;

import com.system.libsystem.entities.cardnumber.CardNumberEntity;
import com.system.libsystem.entities.cardnumber.CardNumberRepository;
import com.system.libsystem.entities.user.UserEntity;
import com.system.libsystem.entities.user.UserRepository;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.exceptions.cardnumber.CardNumberNotFoundException;
import com.system.libsystem.exceptions.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardNumbersService {

    private final CardNumberRepository cardNumberRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public List<CardNumberDTO> getCardNumbers(HttpServletRequest httpServletRequest) {
        userService.validateIfUserIsEnabledByServletRequest(httpServletRequest);
        return getCardNumberDTOs();
    }

    public List<CardNumberDTO> getCardNumberById(Long cardNumberId, HttpServletRequest httpServletRequest) {
        userService.validateIfUserIsEnabledByServletRequest(httpServletRequest);
        if (cardNumberId == null) {
            return getCardNumbers(httpServletRequest);
        }
        return getCardNumberDTOById(cardNumberId);
    }

    public List<CardNumberDTO> getCardNumberByCardNumber(Long cardNumber, HttpServletRequest httpServletRequest) {
        userService.validateIfUserIsEnabledByServletRequest(httpServletRequest);
        if (cardNumber == null) {
            return getCardNumbers(httpServletRequest);
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
