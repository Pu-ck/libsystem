import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-books',
  templateUrl: './books.component.html',
  styleUrls: ['./books.component.css']
})
export class BooksComponent implements OnInit {

  public bookListEmpty: boolean = false;
  public books: any[] = [];

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.getUserBooks();
  }

  private getUserBooks() {
    const url = '/api/userprofile/books';
    this.http.get<any[]>(url, {}).subscribe(
      response => {
        this.books = response;
        if (this.books.length === 0) {
          this.bookListEmpty = true;
        }
      },
      error => {
        console.log(error);
      }
    );
  }

}
