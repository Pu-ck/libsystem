package com.system.libsystem.rest.home;

import com.system.libsystem.entities.newbook.NewBookEntity;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/home")
@AllArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    public List<NewBookEntity> getNewBooks() {
        return homeService.getNewBooks();
    }

}
