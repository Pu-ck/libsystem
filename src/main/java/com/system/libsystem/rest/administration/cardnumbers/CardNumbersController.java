package com.system.libsystem.rest.administration.cardnumbers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/administration/card-numbers")
public class CardNumbersController {

    private final CardNumbersService cardNumbersService;

    @GetMapping
    public List<CardNumberDTO> getCardNumbers(HttpServletRequest httpServletRequest) {
        return cardNumbersService.getCardNumbers(httpServletRequest);
    }

    @GetMapping
    @RequestMapping(params = "cardNumberId")
    public List<CardNumberDTO> getCardNumberById(@RequestParam(required = false) Long cardNumberId,
                                                    HttpServletRequest httpServletRequest) {
        return cardNumbersService.getCardNumberById(cardNumberId, httpServletRequest);
    }

    @GetMapping
    @RequestMapping(params = "cardNumber")
    public List<CardNumberDTO> getCardNumberByCardNumber(@RequestParam(required = false) Long cardNumber,
                                                            HttpServletRequest httpServletRequest) {
        return cardNumbersService.getCardNumberByCardNumber(cardNumber, httpServletRequest);
    }

}
