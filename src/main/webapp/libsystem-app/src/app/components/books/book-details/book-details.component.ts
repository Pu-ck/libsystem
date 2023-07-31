import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonBookMethodsService } from 'src/app/services/book/common-book-methods.service';
import { UserEnabledService } from 'src/app/services/user/user-enabled.service';
import { BookDetails } from 'src/app/models/books/book-details';
import { Observer } from 'rxjs';
import { TranslationService } from 'src/app/services/translation/translation.service';

@Component({
  selector: 'app-book-details',
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.css']
})
export class BookDetailsComponent implements OnInit {

  private bookId: string = '';

  public bookDetails!: BookDetails;
  public booksOnStock: boolean = true;
  public isFavourite: boolean = false;

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
    private userEnabledService: UserEnabledService,
    public translation: TranslationService,
    public commonBookMethodsService: CommonBookMethodsService
  ) { }

  ngOnInit(): void {
    this.getBookDetails();
    this.checkIfBookIsInUsersFavourites();
  }

  public redirectToOrderForm(): void {
    this.router.navigate([`/books/${this.bookId}/borrow-book`]);
  }

  public addToFavourites(): void {
    const url = `/api/books/${this.bookId}/add-to-favourites`;
    const observer: Observer<any> = {
      next: (response) => {
        console.log(response);
        this.isFavourite = true;
      },
      error: (error) => {
        this.userEnabledService.validateIfUserIsEnabled(error);
      },
      complete: () => {
      },
    };

    this.http.post<any>(url, {}).subscribe(observer);
  }

  public removeFromFavourites(): void {
    const url = `/api/books/${this.bookId}/remove-from-favourites`;
    const observer: Observer<any> = {
      next: (response) => {
        console.log(response);
        this.isFavourite = false;
      },
      error: (error) => {
        this.userEnabledService.validateIfUserIsEnabled(error);
      },
      complete: () => {
      },
    };

    this.http.delete<any>(url, {}).subscribe(observer);
  }

  private getBookDetails(): void {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.bookId = params.get('id')!;
    });
    const url = `api/books/${this.bookId}`;
    const observer: Observer<any> = {
      next: (response) => {
        this.bookDetails = response;
        this.checkIfBooksOnStock();
      },
      error: (error) => {
        if (error.status === 404 && error.error.message === 'Book not found' || error.status === 400) {
          console.log(error);
          this.router.navigate(['/']);
        }
      },
      complete: () => {
      },
    };

    this.http.get<any>(url, {}).subscribe(observer);
  }

  private checkIfBooksOnStock(): void {
    this.booksOnStock = this.commonBookMethodsService.areBooksOnStock(this.bookDetails);
  }


  private checkIfBookIsInUsersFavourites(): void {
    const url = 'api/userprofile/favourites';
    const observer: Observer<any> = {
      next: (response) => {
        for (let favouriteBook of response) {
          if (favouriteBook.bookId.toString() === this.bookId) {
            this.isFavourite = true;
          }
        }
      },
      error: (error) => {
        console.log(error);
      },
      complete: () => {
      },
    };

    this.http.get<any>(url, {}).subscribe(observer);
  }

}
