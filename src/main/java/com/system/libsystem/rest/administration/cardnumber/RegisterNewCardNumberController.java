package com.system.libsystem.rest.administration.cardnumber;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/administration/card-numbers/register-new-card-number")
public class RegisterNewCardNumberController {

    private final RegisterNewCardNumberService registerNewCardNumberService;

    @PostMapping
    public void registerNewCardNumber(@RequestBody RegisterNewCardNumberRequest registerNewCardNumberRequest) {
        registerNewCardNumberService.registerNewCardNumber(registerNewCardNumberRequest);
    }

}
