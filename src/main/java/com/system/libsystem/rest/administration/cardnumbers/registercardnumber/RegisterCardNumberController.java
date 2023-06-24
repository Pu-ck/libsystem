package com.system.libsystem.rest.administration.cardnumbers.registercardnumber;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("/api/administration/card-numbers/register-card-number")
public class RegisterCardNumberController {

    private final RegisterCardNumberService registerCardNumberService;

    @PostMapping
    public void registerNewCardNumber(@RequestBody RegisterCardNumberRequest registerCardNumberRequest,
                                      HttpServletRequest httpServletRequest) {
        registerCardNumberService.registerNewCardNumber(registerCardNumberRequest, httpServletRequest);
    }

}
