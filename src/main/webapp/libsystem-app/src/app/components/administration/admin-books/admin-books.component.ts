import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { UserEnabledService } from 'src/app/services/user/user-enabled.service';

@Component({
  selector: 'app-administration-books',
  templateUrl: './admin-books.component.html',
  styleUrls: ['./admin-books.component.css']
})
export class AdminBooksComponent implements OnInit {

  public borrowedBooks: any[] = [];

  public showAccepted: boolean = false;
  public showExtended: boolean = false;
  public showReady: boolean = false;
  public showClosed: boolean = false;
  public showAll: boolean = true;

  public borrowedBookNotFound: boolean = false;

  public searchType: string = '';
  public searchValue: string = '';

  public model: any = {};

  constructor(
    private http: HttpClient,
    private router: Router,
    private userEnabledService: UserEnabledService
  ) { }

  ngOnInit(): void {
    this.getBorrowedBooks();
  }

  public getBorrowedBooks(): void {
    const url = '/api/administration/books';
    let params = this.getSearchType();
    this.http.get<any[]>(url, {params}).subscribe(
      response => {
        this.borrowedBooks = response;
        this.borrowedBookNotFound = false;
        let queryParams: {[key: string]: string} = {};
        this.setQueryParams(queryParams);
        this.router.navigate(['/administration/books'], { queryParams });
        if (this.borrowedBooks.length === 0) {
          this.borrowedBookNotFound = true;
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
    this.http.put<any>(url, { accepted: accepted
    }).subscribe(response => {
      console.log(response);
      sessionStorage.setItem('hasUpdatedOrderReadyStatus', 'true');
      window.location.reload();
    }, error => {
      console.log(error);
    }
    );
  }

  public acceptOrder(borrowedBookId: string, cardNumber: string): void {
    const url = `/api/administration/books/${borrowedBookId}/accept-order`
    this.http.put<any>(url, { cardNumber: cardNumber
    }).subscribe(response => {
      console.log(response);
      sessionStorage.setItem('hasAcceptedOrder', 'true');
      window.location.reload();
    }, error => {
      console.log(error);
    }
    );
  }

  public returnBook(borrowedBookId: string, cardNumber: string): void {
    const url = `/api/administration/books/${borrowedBookId}/return-book`
    this.http.put<any>(url, { cardNumber: cardNumber
    }).subscribe(response => {
      console.log(response);
      sessionStorage.setItem('hasReturnedBook', 'true');
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

  private setQueryParams(queryParams: {[key: string]: string}): void {
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
