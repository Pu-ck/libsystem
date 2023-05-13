import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { CommonBookMethodsService } from '../../services/book/common-book-methods.service';
import genres from 'src/config/genres.json';
import affiliates from 'src/config/affiliates.json';

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
  public selectedGenres: any[] = genres;
  
  public affiliates: string[] = [];
  public selectedAffiliates: any[] = affiliates;

  constructor(
    private http: HttpClient, 
    private router: Router,
    private route: ActivatedRoute,
    public commonBookMethodsService: CommonBookMethodsService
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
    if (this.selectedGenres[0].value) {
      genres.push(this.selectedGenres[0].label);
    }
    if (this.selectedGenres[1].value) {
      genres.push(this.selectedGenres[1].label);
    }
    if (this.selectedGenres[2].value) {
      genres.push(this.selectedGenres[2].label);
    }
    this.genres = genres;
  }

  public updateAffiliates() {
    let affiliates = [];
    if (this.selectedAffiliates[0].value) {
      affiliates.push(this.selectedAffiliates[0].label);
    }
    if (this.selectedAffiliates[1].value) {
      affiliates.push(this.selectedAffiliates[1].label);
    }
    this.affiliates = affiliates;
  }
  

}
