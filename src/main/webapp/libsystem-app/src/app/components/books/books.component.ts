import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute  } from '@angular/router';

@Component({
  selector: 'app-books',
  templateUrl: './books.component.html',
  styleUrls: ['./books.component.css']
})
export class BooksComponent implements OnInit {

  public books: any[] = [];
  public title: string = '';
  public author: string = '';
  public genre: string = '';
  public publisher: string = '';
  public yearOfPrint: string = '';
  public affiliate: string = '';
  public sortType: string = '';
  public sortDirection: string = '';

  constructor(
    private http: HttpClient, 
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.getFilterParams()
    this.getBooks();
  }

  public getBooks() {
    const url = `/api/books?title=${this.title}&author=${this.author}&genre=${this.genre}&publisher=${this.publisher}&yearOfPrint=${this.yearOfPrint}&affiliate=${this.affiliate}&sortType=${this.sortType}&sortDirection=${this.sortDirection}`;

    this.http.get<any[]>(url, {}).subscribe(
      response => {
        this.books = response;
      },
      error => {
        console.log(error);
      }
    );
  }

  public redirectToBookDetails(id: number) {
    this.router.navigate([`books/${id}`]);
  }

  private getFilterParams() {
    this.title = this.route.snapshot.queryParamMap.get('title')! || '';
    this.author = this.route.snapshot.queryParamMap.get('author')! || '';
    this.genre = this.route.snapshot.queryParamMap.get('genre')! || '';
    this.publisher = this.route.snapshot.queryParamMap.get('publisher')! || '';
    this.yearOfPrint = this.route.snapshot.queryParamMap.get('yearOfPrint')! || '';
    this.affiliate = this.route.snapshot.queryParamMap.get('affiliate')! || '';
    this.sortType = this.route.snapshot.queryParamMap.get('sortType')! || '';
    this.sortDirection = this.route.snapshot.queryParamMap.get('sortDirection')! || '';
  }

}
