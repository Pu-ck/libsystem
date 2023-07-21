import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserEnabledService } from 'src/app/services/user/user-enabled.service';
import { PaginationService } from 'src/app/services/pagination/pagination.service';

@Component({
  selector: 'app-user-books',
  templateUrl: './user-books.component.html',
  styleUrls: ['./user-books.component.css']
})
export class UserBooksComponent implements OnInit {

  public currentPage: number = 1;
  public itemsPerPage: number = 10;
  public bookExtended: boolean = false;
  public displayedStatus: string = 'All';
  public books: any[] = [];
  public borrowedBookId!: number;
  public display = "none";
  public hasSentBookExtensionRequest: string = 'false';

  constructor(
    private http: HttpClient,
    private userEnabledService: UserEnabledService,
    public pagination: PaginationService
  ) { }

  ngOnInit(): void {
    this.checkIfTheBookHasBeenSuccessfullyExtended();
    this.getUserBooks();
  }

  public requestBookExtension(borrowedBookId: number): void {
    const url = `/api/userprofile/books/${borrowedBookId}/extend-book`
    this.http.put<any>(url, {
    }).subscribe(response => {
      this.bookExtended = true;
      this.display = 'none'
      localStorage.setItem('hasSentBookExtensionRequest', 'true');
      window.location.reload();
      console.log(response);
    }, error => {
      this.userEnabledService.validateIfUserIsEnabled(error);
      this.display = 'none';
    }
    );
  }

  public setDisplayedStatus(status: string): void {
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
    this.display = "block";
    this.bookExtended = false;
  }

  public onCloseHandled(): void {
    this.display = "none";
  }

  public onPageChange(page: number): void {
    this.currentPage = page;
    this.bookExtended = false;
  }

  private getUserBooks(): void {
    const url = '/api/userprofile/books';
    this.http.get<any[]>(url, {}).subscribe(
      response => {
        this.books = response;
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
