package com.system.libsystem.rest.home;

import com.system.libsystem.entities.newbook.NewBookEntity;
import com.system.libsystem.entities.newbook.NewBookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class HomeService {

    private final NewBookRepository newBookRepository;

    public List<NewBookEntity> getNewBooks() {
        return newBookRepository.findAll();
    }

}
