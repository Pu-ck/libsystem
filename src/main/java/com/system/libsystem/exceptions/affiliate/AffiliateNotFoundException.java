package com.system.libsystem.exceptions.affiliate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AffiliateNotFoundException extends RuntimeException {

    private final String name;

    @Override
    public String getMessage() {
        return "Unable to find affiliate " + name;
    }

}
