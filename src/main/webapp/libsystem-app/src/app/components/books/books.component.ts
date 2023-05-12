import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-books',
  templateUrl: './books.component.html',
  styleUrls: ['./books.component.css']
})
export class BooksComponent implements OnInit {

  public books: any[] = [];
  public noResultsFound: boolean = false;
  public title: string = '';
  public author: string = '';
  public publisher: string = '';
  public yearOfPrint: string = '';
  public sortType: string = 'title';
  public sortDirection: string = 'asc';

  public genres: string[] = [];
  public genre1: boolean = false;
  public genre2: boolean = false;
  public genre3: boolean = false;

  public affiliates: string[] = [];
  public affiliateA: boolean = false;
  public affiliateB: boolean = false;

  constructor(
    private http: HttpClient, 
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.title = params['title'] || '';
      this.author = params['author'] || '';
      this.genres = params['genres'] || '';
      this.publisher = params['publisher'] || '';
      this.yearOfPrint = params['yearOfPrint'] || '';
      this.affiliates = params['affiliates'] || '';
      this.sortType = params['sortType'] || '';
      this.sortDirection = params['sortDirection'] || '';
      this.getBooks();
    });
  }

  public getBooks() {

    this.updateGenres();
    this.updateAffiliates();

    const url = '/api/books';
    let params = new HttpParams().set('title', this.title).set('author', this.author).set('genres', this.genres.join(',')).set('publisher', this.publisher).set('yearOfPrint', this.yearOfPrint).set('affiliates', this.affiliates.join(',')).set('sortType', this.sortType).set('sortDirection', this.sortDirection);

    this.http.get<any[]>(url, {params}).subscribe(
      response => {
        this.books = response;
        if (this.books.length === 0) {
          this.noResultsFound = true;
        } else {
          this.noResultsFound = false;
        }
        let queryParams: {[key: string]: string} = {};
        this.setQueryParams(queryParams);
        this.router.navigate(['/books'], { queryParams });
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

  public getAffiliateBookQuantity(book: any, affiliateId: number): string {
    const affiliateBook = book.affiliateBooks.find((ab: any) => ab.affiliateId === affiliateId);
    return affiliateBook ? `${affiliateBook.currentQuantity}/${affiliateBook.generalQuantity}` : '';
  }

  private setQueryParams(queryParams: {[key: string]: string}) {
    if (this.title) {
      queryParams['title'] = this.title;
    }
    if (this.author) {
      queryParams['author'] = this.author;
    }
    if (this.genres.length !== 0) {
      queryParams['genres'] = this.genres.join(',');
    }
    if (this.publisher) {
      queryParams['publisher'] = this.publisher;
    }
    if (this.yearOfPrint) {
      queryParams['yearOfPrint'] = this.yearOfPrint;
    }
    if (this.affiliates.length !== 0) {
      queryParams['affiliates'] = this.affiliates.join(',');
    }
    if (this.sortType) {
      queryParams['sortType'] = this.sortType;
    }
    if (this.sortDirection) {
      queryParams['sortDirection'] = this.sortDirection;
    }
  }

  public updateGenres() {
    let genres = [];
    if (this.genre1) {
      genres.push('Genre 1');
    }
    if (this.genre2) {
      genres.push('Genre 2');
    }
    if (this.genre3) {
      genres.push('Genre 3');
    }
    this.genres = genres;
  }

  public updateAffiliates() {
    let affiliates = [];
    if (this.affiliateA) {
      affiliates.push('Affiliate A');
    }
    if (this.affiliateB) {
      affiliates.push('Affiliate B');
    }
    this.affiliates = affiliates;
  }
  

}
