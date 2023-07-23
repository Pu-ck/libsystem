import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserEnabledService } from 'src/app/services/user/user-enabled.service';
import { PaginationService } from 'src/app/services/pagination/pagination.service';
import { Book } from 'src/app/models/books/book';
import { CommonConstants } from 'src/app/utils/common-constants';

@Component({
  selector: 'app-user-books',
  templateUrl: './user-books.component.html',
  styleUrls: ['./user-books.component.css']
})
export class UserBooksComponent implements OnInit {

  public currentPage: number = CommonConstants.DEFAULT_PAGE_NUMBER;
  public itemsPerPage: number = CommonConstants.ITEMS_PER_PAGE;

  public bookExtended: boolean = false;
  public noBooksWithStatusFound: boolean = false;

  public displayedStatus: string = 'All';
  public hasSentBookExtensionRequest: string = 'false';
  public display: string = CommonConstants.MODAL_DISPLAY_HIDE;

  public books: Book[] = [];
  public borrowedBookId!: number;  

  constructor(
    private http: HttpClient,
    private userEnabledService: UserEnabledService,
    public pagination: PaginationService
  ) { }

  ngOnInit(): void {
    this.checkIfTheBookHasBeenSuccessfullyExtended();
    this.getUserBooks('All');
  }

  public requestBookExtension(borrowedBookId: number): void {
    const url = `/api/userprofile/books/${borrowedBookId}/extend-book`
    this.http.put<any>(url, {
    }).subscribe(response => {
      this.bookExtended = true;
      this.display = CommonConstants.MODAL_DISPLAY_HIDE;
      localStorage.setItem('hasSentBookExtensionRequest', 'true');
      window.location.reload();
      console.log(response);
    }, error => {
      this.userEnabledService.validateIfUserIsEnabled(error);
      this.display = CommonConstants.MODAL_DISPLAY_HIDE;
    }
    );
  }

  public setDisplayedStatus(status: string): void {
    this.currentPage = CommonConstants.DEFAULT_PAGE_NUMBER;
    this.getUserBooks(status);
    if (status === 'Borrowed') {
      this.displayedStatus = 'Borrowed';
      this.bookExtended = false;
    } else if (status === 'Ordered') {
      this.displayedStatus = 'Ordered';
      this.bookExtended = false;
    } else if (status === 'Returned') {
      this.displayedStatus = 'Returned';
      this.bookExtended = false;
    } else if (status === 'Rejected') {
      this.displayedStatus = 'Rejected';
      this.bookExtended = false;
    } else if (status === 'Ready') {
      this.displayedStatus = 'Ready';
      this.bookExtended = false;
    } else if (status === 'All') {
      this.displayedStatus = 'All';
      this.bookExtended = false;
    }
  }

  public openModal(): void {
    this.display = CommonConstants.MODAL_DISPLAY_SHOW;
    this.bookExtended = false;
  }

  public onCloseHandled(): void {
    this.display = CommonConstants.MODAL_DISPLAY_HIDE;
  }

  public onPageChange(page: number): void {
    this.currentPage = page;
    this.bookExtended = false;
  }

  public getRemainingDays(returnDate: string): number {
    const timeDifference = new Date(returnDate).getTime() - new Date().getTime();
    const daysDifference = timeDifference / (1000 * 3600 * 24);
    return Math.round(daysDifference);
  }

  private getUserBooks(status: string): void {
    const url = '/api/userprofile/books';
    this.http.get<any[]>(url, {}).subscribe(
      response => {

        if (status === 'All') {
          this.books = response;
        }
        if (status === 'Ordered') {
          this.books = response.filter(book => book.status === 'Ordered');
        }
        if (status === 'Borrowed') {
          this.books = response.filter(book => book.status === 'Borrowed');
        }
        if (status === 'Returned') {
          this.books = response.filter(book => book.statu === 'Returned');
        }
        if (status === 'Rejected') {
          this.books = response.filter(book => book.status === 'Rejected');
        }
        if (status === 'Ready') {
          this.books = response.filter(book => book.status === 'Ready');
        }

        if (this.books.length === 1) {
          this.currentPage = CommonConstants.DEFAULT_PAGE_NUMBER;
        }
        if (this.books.length === 0) {
          this.noBooksWithStatusFound = true;
        } else {
          this.noBooksWithStatusFound = false;
        }

      },
      error => {
        this.userEnabledService.validateIfUserIsEnabled(error);
      }
    );
  }

  private checkIfTheBookHasBeenSuccessfullyExtended(): void {
    this.hasSentBookExtensionRequest = localStorage.getItem('hasSentBookExtensionRequest') || 'false';
    if (this.hasSentBookExtensionRequest === 'true') {
      this.bookExtended = true;
      localStorage.setItem('hasSentBookExtensionRequest', 'false');
    }
  }

}
