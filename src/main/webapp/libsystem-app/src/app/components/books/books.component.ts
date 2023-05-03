import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-books',
  templateUrl: './books.component.html',
  styleUrls: ['./books.component.css']
})
export class BooksComponent implements OnInit {

  public books: any[] = [];
  public noResultsFound: boolean = false;
  public selectedSearchParam:string = 'title';
  public searchedValue: string = '';
  public selectedSortType: string = '1';
  public selectedSortDirection: string = '';
  public genre: string = '';
  public affiliate: string = '';

  constructor(
    private http: HttpClient, 
    private router: Router
  ) { }

  ngOnInit(): void {
    this.getBooks();
  }

  public getBooks() {
    const url = `/api/books?${this.selectedSearchParam}=${this.searchedValue}&title=&author=&genre=${this.genre}&publisher=&yearOfPrint=&affiliate=${this.affiliate}&sortType=${this.selectedSortType}&sortDirection=${this.selectedSortDirection}`;

    this.http.get<any[]>(url, {}).subscribe(
      response => {
        console.log(url);
        this.books = response;
        if (this.books.length == 0) {
          this.noResultsFound = true;
        } else {
          this.noResultsFound = false;
        }
      },
      error => {
        this.noResultsFound = true;
        this.books = [];
        console.log(error);
      }
    );
  }

  public redirectToBookDetails(id: number) {
    this.router.navigate([`books/${id}`]);
  }

}
