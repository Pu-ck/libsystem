package com.system.libsystem.helpermodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
