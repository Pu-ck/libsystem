import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserEnabledService } from 'src/app/services/user/user-enabled.service';

@Component({
  selector: 'app-user-books',
  templateUrl: './user-books.component.html',
  styleUrls: ['./user-books.component.css']
})
export class UserBooksComponent implements OnInit {

  public bookExtended: boolean = false;
  public displayedStatus: string = 'All';
  public books: any[] = [];

  constructor(
    private http: HttpClient,
    private userEnabledService: UserEnabledService
    ) { }

  ngOnInit(): void {
    this.getUserBooks();
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

  public bookExtensionConfirmation(borrowedBookId: number): void {
    if (window.confirm('Are you sure you want to send a return date extension request for this book? You can extend the borrowed book only once.')) {
      this.requestBookExtension(borrowedBookId);
    }
  }

  private requestBookExtension(borrowedBookId: number): void {
    const url = `/api/userprofile/books/${borrowedBookId}/extend-book`
    this.http.put<any>(url, {
    }).subscribe(response => {
      this.bookExtended = true;
      console.log(response);
    }, error => {
      this.userEnabledService.validateIfUserIsEnabled(error);
    }
    );
  }

  public setDisplayedStatus(status: string): void {
    if (status === 'Borrowed') {
      this.displayedStatus = 'Borrowed';
    } else if (status === 'Ordered') {
      this.displayedStatus = 'Ordered';
    } else if (status === 'Returned') {
      this.displayedStatus = 'Returned';
    } else if (status === 'Rejected') {
      this.displayedStatus = 'Rejected';
    } else if (status === 'Ready') {
      this.displayedStatus = 'Ready';
    } else if (status === 'All') {
      this.displayedStatus = 'All';
    }
  }

}
