import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { UserEnabledService } from 'src/app/services/user/user-enabled.service';
import { PaginationService } from 'src/app/services/pagination/pagination.service';

@Component({
  selector: 'app-administration-books',
  templateUrl: './admin-books.component.html',
  styleUrls: ['./admin-books.component.css']
})
export class AdminBooksComponent implements OnInit {

  public currentPage: number = 1;
  public itemsPerPage: number = 20;

  public borrowedBooks: any[] = [];

  public showAccepted: boolean = false;
  public showExtended: boolean = false;
  public showReady: boolean = false;
  public showClosed: boolean = false;
  public showAll: boolean = true;

  public bookReturned: boolean = false;
  public bookAccepted: boolean = false;
  public bookRejected: boolean = false;
  public bookReady: boolean = false;

  public borrowedBookId: string = localStorage.getItem('borrowedBookId') || '';
  public borrowedBookCardNumber!: string;

  public borrowedBookNotFound: boolean = false;

  public searchType: string = '';
  public searchValue: string = '';

  public model: any = {};

  public displayAcceptModal = "none";
  public displayReadyModal = "none";
  public displayRejectModal = "none";
  public displayReturnModal = "none";

  constructor(
    private http: HttpClient,
    private router: Router,
    private userEnabledService: UserEnabledService,
    public pagination: PaginationService
  ) { }

  ngOnInit(): void {
    this.checkIfBookStatusHasBeenChanged();
    this.getBorrowedBooks('All');
  }

  public getBorrowedBooks(status: string): void {
    const url = '/api/administration/books';
    let params = this.getSearchType();
    this.http.get<any[]>(url, { params }).subscribe(
      response => {
        
        if (status === 'All') {
          this.borrowedBooks = response;
        }
        if (status === 'Accepted') {
          this.borrowedBooks = response.filter(borrowedBook => borrowedBook.accepted === true);
        }
        if (status === 'Closed') {
          this.borrowedBooks = response.filter(borrowedBook => borrowedBook.closed === true);
        }
        if (status === 'Ready') {
          this.borrowedBooks = response.filter(borrowedBook => borrowedBook.ready === true);
        }
        if (status === 'Extended') {
          this.borrowedBooks = response.filter(borrowedBook => borrowedBook.extended === true);
        }

        let queryParams: { [key: string]: string } = {};
        this.setQueryParams(queryParams);
        this.router.navigate(['/administration/books'], { queryParams });
        if (this.borrowedBooks.length === 0) {
          this.borrowedBookNotFound = true;
        } else {
          this.borrowedBookNotFound = false;
        }
        if (this.borrowedBooks.length === 1) {
          this.currentPage = 1;
        }
      },
      error => {
        this.userEnabledService.validateIfUserIsEnabled(error);
        if (error.status === 404 && error.error.message === 'Borrowed book not found') {
          this.borrowedBookNotFound = true;
          this.borrowedBooks = [];
          console.log(error);
        }
      }
    );
  }

  public updateOrderReadyStatus(borrowedBookId: string, accepted: boolean): void {
    const url = `/api/administration/books/${borrowedBookId}/ready-order`
    this.http.put<any>(url, {
      accepted: accepted
    }).subscribe(response => {
      console.log(response);
      if (accepted) {
        localStorage.setItem('bookReady', 'true');
      } else {
        localStorage.setItem('bookRejected', 'true');
      }
      localStorage.setItem('borrowedBookId', borrowedBookId);
      window.location.reload();
    }, error => {
      console.log(error);
    }
    );
  }

  public acceptOrder(borrowedBookId: string, cardNumber: string): void {
    const url = `/api/administration/books/${borrowedBookId}/accept-order`
    this.http.put<any>(url, {
      cardNumber: cardNumber
    }).subscribe(response => {
      console.log(response);
      localStorage.setItem('bookAccepted', 'true');
      localStorage.setItem('borrowedBookId', borrowedBookId);
      window.location.reload();
    }, error => {
      console.log(error);
    }
    );
  }

  public returnBook(borrowedBookId: string, cardNumber: string): void {
    const url = `/api/administration/books/${borrowedBookId}/return-book`
    this.http.put<any>(url, {
      cardNumber: cardNumber
    }).subscribe(response => {
      console.log(response);
      localStorage.setItem('bookReturned', 'true');
      localStorage.setItem('borrowedBookId', borrowedBookId);
      window.location.reload();
    }, error => {
      console.log(error);
    }
    );
  }

  public redirectToExtendBorrowedBookForm(id: number): void {
    this.router.navigate([`administration/books/${id}/extend-book`]);
  }

  public setDisplayedStatus(status: string): void {
    this.clearAllSuccessfulActionsLabels();
    this.currentPage = 1;
    this.getBorrowedBooks(status);
    if (status === 'Accepted') {
      this.showAccepted = true;
      this.showClosed = false;
      this.showReady = false;
      this.showExtended = false;
      this.showAll = false;
    } else if (status === 'Closed') {
      this.showClosed = true;
      this.showAccepted = false;
      this.showReady = false;
      this.showExtended = false;
      this.showAll = false;
    } else if (status === 'Ready') {
      this.showReady = true;
      this.showClosed = false;
      this.showAccepted = false;
      this.showExtended = false;
      this.showAll = false;
    } else if (status === 'Extended') {
      this.showExtended = true;
      this.showClosed = false;
      this.showReady = false;
      this.showAccepted = false;
      this.showAll = false;
    } else if (status === 'All') {
      this.showAll = true;
      this.showClosed = false;
      this.showReady = false;
      this.showExtended = false;
      this.showAccepted = false;
    }
  }

  public openModalByType(type: string): void {
    this.clearAllSuccessfulActionsLabels();
    if (type === 'accept') {
      this.displayAcceptModal = 'block';
    }
    if (type === 'ready') {
      this.displayReadyModal = 'block';
    }
    if (type === 'reject') {
      this.displayRejectModal = 'block';
    }
    if (type === 'return') {
      this.displayReturnModal = 'block';
    }
  }

  public onPageChange(page: number): void {
    this.currentPage = page;
    this.clearAllSuccessfulActionsLabels();
  }

  public onCloseHandled(): void {
    this.displayAcceptModal = 'none';
    this.displayReadyModal = 'none';
    this.displayRejectModal = 'none';
    this.displayReturnModal = 'none';
  }

  private checkIfBookStatusHasBeenChanged(): void {
    if (localStorage.getItem('bookAccepted') === 'true') {
      this.bookAccepted = true;
      localStorage.setItem('bookAccepted', 'false');
    }
    if (localStorage.getItem('bookRejected') === 'true') {
      this.bookRejected = true;
      localStorage.setItem('bookRejected', 'false');
    }
    if (localStorage.getItem('bookReturned') === 'true') {
      this.bookReturned = true;
      localStorage.setItem('bookReturned', 'false');
    }
    if (localStorage.getItem('bookReady') === 'true') {
      this.bookReady = true;
      localStorage.setItem('bookReady', 'false');
    }
  }

  private clearAllSuccessfulActionsLabels(): void {
    this.bookAccepted = false;
    this.bookReady = false;
    this.bookRejected = false;
    this.bookReturned = false;
  }

  private getSearchType(): HttpParams {
    if (this.searchType === 'borrowedBookId') {
      return new HttpParams().set('borrowedBookId', this.searchValue);
    }
    if (this.searchType === 'bookId') {
      return new HttpParams().set('bookId', this.searchValue);
    }
    if (this.searchType === 'userId') {
      return new HttpParams().set('userId', this.searchValue);
    }
    if (this.searchType === 'cardNumber') {
      return new HttpParams().set('cardNumber', this.searchValue);
    }
    return new HttpParams().set('', '');
  }

  private setQueryParams(queryParams: { [key: string]: string }): void {
    if (this.searchType === 'borrowedBookId') {
      queryParams['borrowedBookId'] = this.searchValue;
    }
    if (this.searchType === 'userId') {
      queryParams['userId'] = this.searchValue;
    }
    if (this.searchType === 'bookId') {
      queryParams['bookId'] = this.searchValue;
    }
    if (this.searchType === 'cardNumber') {
      queryParams['cardNumber'] = this.searchValue;
    }
  }

}
