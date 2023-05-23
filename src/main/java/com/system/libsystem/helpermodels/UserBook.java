package com.system.libsystem.helpermodels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBook {
    private String title;
    private Long borrowedBookId;
    private String bookDetailsLink;
    private String borrowDate;
    private String returnDate;
    private String readyDate;
    private String penalty;
    private String affiliate;
    private String status;
    private boolean extended;
    private boolean accepted;
    private boolean closed;
}
