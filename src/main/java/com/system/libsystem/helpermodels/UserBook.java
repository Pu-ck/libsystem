package com.system.libsystem.helpermodels;

import com.system.libsystem.entities.author.AuthorEntity;
import com.system.libsystem.entities.genre.GenreEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserBook {
    private String title;
    private Set<AuthorEntity> authors = new HashSet<>();
    private Set<GenreEntity> genres = new HashSet<>();
    private String publisherName;
    private String yearOfPrint;
    private String borrowDate;
    private String returnDate;
    private String readyDate;
    private String penalty;
    private String affiliate;
    private String status;
}
