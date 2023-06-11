package com.system.libsystem.rest.userprofile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavouriteBookDTO {
    private Long bookId;
    private String authors;
    private String title;
}
