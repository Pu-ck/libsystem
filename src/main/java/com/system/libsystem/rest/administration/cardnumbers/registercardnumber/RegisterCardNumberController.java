package com.system.libsystem.rest.administration.cardnumbers.registercardnumber;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/administration/card-numbers/register-card-number")
public class RegisterCardNumberController {

    private final RegisterCardNumberService registerCardNumberService;

    @PostMapping
    public void registerNewCardNumber(@RequestBody RegisterCardNumberRequest registerCardNumberRequest) {
        registerCardNumberService.registerNewCardNumber(registerCardNumberRequest);
    }

}
