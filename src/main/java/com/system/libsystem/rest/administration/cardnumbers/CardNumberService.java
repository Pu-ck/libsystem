package com.system.libsystem.rest.administration.cardnumbers;

import java.util.List;

public interface CardNumberService {

    List<CardNumberDTO> getCardNumbers();

    List<CardNumberDTO> getCardNumberById(Long cardNumberId);

    List<CardNumberDTO> getCardNumberByCardNumber(Long cardNumber);

}
