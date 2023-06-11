package com.system.libsystem.rest.home;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@RestController
@RequestMapping("/api/home")
@AllArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    public Set<NewBookDTO> getNewBooks(HttpServletRequest httpServletRequest) {
        return homeService.getNewBooks(httpServletRequest);
    }

}
