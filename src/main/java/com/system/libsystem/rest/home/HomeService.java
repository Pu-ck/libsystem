package com.system.libsystem.rest.home;

import com.system.libsystem.entities.book.BookEntity;
import com.system.libsystem.entities.book.BookRepository;
import com.system.libsystem.entities.newbook.NewBookEntity;
import com.system.libsystem.entities.newbook.NewBookRepository;
import com.system.libsystem.entities.user.UserService;
import com.system.libsystem.exceptions.book.BookNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final BookRepository bookRepository;
    private final NewBookRepository newBookRepository;
    private final UserService userService;

    public Set<NewBookDTO> getNewBooks(HttpServletRequest httpServletRequest) {
        userService.validateIfUserIsEnabledByServletRequest(httpServletRequest);
        return getNewBookDTOs();
    }

    private Set<NewBookDTO> getNewBookDTOs() {
        final Set<NewBookDTO> newBookDTOs = new HashSet<>();
        for (NewBookEntity newBookEntity : newBookRepository.findAll()) {
            final NewBookDTO newBookDTO = new NewBookDTO();
            final BookEntity bookEntity = bookRepository.findById(newBookEntity.getBookId()).orElseThrow(() ->
                    new BookNotFoundException(newBookEntity.getBookId()));
            newBookDTO.setBookId(newBookEntity.getBookId());
            newBookDTO.setTitle(bookEntity.getTitle());
            newBookDTO.setAuthors(bookEntity.getFormattedAuthorsAsString());
            newBookDTO.setGenres(bookEntity.getFormattedGenresAsString());
            newBookDTO.setPublisher(bookEntity.getPublisherEntity().getName());
            newBookDTO.setYearOfPrint(String.valueOf(bookEntity.getYearOfPrintEntity().getYearOfPrint()));
            newBookDTOs.add(newBookDTO);
        }
        return newBookDTOs;
    }

}
