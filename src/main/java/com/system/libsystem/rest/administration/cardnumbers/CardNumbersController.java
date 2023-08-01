package com.system.libsystem.rest.administration.cardnumbers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/administration/card-numbers")
public class CardNumbersController {

    private final CardNumbersServiceImpl cardNumbersServiceImpl;

    @GetMapping
    public List<CardNumberDTO> getCardNumbers() {
        return cardNumbersServiceImpl.getCardNumbers();
    }

    @GetMapping
    @RequestMapping(params = "cardNumberId")
    public List<CardNumberDTO> getCardNumberById(@RequestParam(required = false) Long cardNumberId) {
        return cardNumbersServiceImpl.getCardNumberById(cardNumberId);
    }

    @GetMapping
    @RequestMapping(params = "cardNumber")
    public List<CardNumberDTO> getCardNumberByCardNumber(@RequestParam(required = false) Long cardNumber) {
        return cardNumbersServiceImpl.getCardNumberByCardNumber(cardNumber);
    }

}
