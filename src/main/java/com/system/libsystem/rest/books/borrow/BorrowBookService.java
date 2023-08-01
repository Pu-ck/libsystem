package com.system.libsystem.rest.books.borrow;

import javax.servlet.http.HttpServletRequest;

public interface BorrowBookService {

    void borrow(BorrowBookRequest borrowBookRequest, HttpServletRequest httpServletRequest);

}
