package com.system.libsystem.rest.login;

import com.system.libsystem.dto.ResponseDTO;
import com.system.libsystem.dto.UserDTO;
import com.system.libsystem.session.SessionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class AuthController {
    @Autowired
    public AuthenticationManager authenticationManager;
    @Autowired
    public SessionRegistry sessionRegistry;

    @PostMapping
    public ResponseEntity<ResponseDTO> login(@RequestBody UserDTO user) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        final String sessionID = sessionRegistry.registerSession(user.getUsername());
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setSessionID(sessionID);

        return ResponseEntity.ok(responseDTO);
    }
}
