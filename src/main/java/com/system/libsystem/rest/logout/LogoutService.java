package com.system.libsystem.rest.logout;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface LogoutService {

    ResponseEntity<Void> logout(HttpServletRequest httpServletRequest);

}
