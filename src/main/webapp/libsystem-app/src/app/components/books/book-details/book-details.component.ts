import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonBookMethodsService } from '../../../services/book/common-book-methods.service';

@Component({
  selector: 'app-book-details',
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.css']
})
export class BookDetailsComponent implements OnInit {

  private bookId: string = '';

  public bookDetails: any;
  public booksOnStock: boolean = true;
  public isFavourite: boolean = false;

  constructor(
    private http: HttpClient, 
    private router: Router,
    private route: ActivatedRoute,
    public commonBookMethodsService: CommonBookMethodsService
  ) { }

  ngOnInit(): void {
    this.getBookDetails();
    this.checkIfBookIsInUsersFavourites();
  }

  public redirectToOrderForm(): void {
    this.router.navigate([`/books/${this.bookId}/borrow-book`]);
  }

  private getBookDetails(): void {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.bookId = params.get('id')!;
    });

    const url = `api/books/${this.bookId}`;
    this.http.get<any>(url, { }).subscribe(response => {
      this.bookDetails = response;
      this.checkIfBooksOnStock();
    }, error => {
        if (error.status === 404 && error.error.message === 'Book not found') {
            console.log(error);
            this.router.navigate(['/']);
        }
      }
    );
  }

  public addToFavourites(): void {
    const url = `/api/books/${this.bookId}/add-to-favourites`;
    this.http.post<any>(url, {
    }).subscribe(response => {
        console.log(response);
        this.isFavourite = true;
    }, error => {
        console.log(error);
    }
    );
  }

  public removeFromFavourites(): void {
    const url = `/api/books/${this.bookId}/remove-from-favourites`;
    this.http.delete<any>(url, { }).subscribe(response => {
        console.log(response);
        this.isFavourite = false;
    }, error => {
        console.log(error);
    }
    );
  }

  private checkIfBookIsInUsersFavourites(): void {
    const url = 'api/userprofile/favourites';
    this.http.get<any>(url, { }).subscribe(response => {
      for (let favouriteBook of response) {
        if (favouriteBook.id.toString() === this.bookId) {
          this.isFavourite = true;
        } 
      }
    }, error => {
        console.log(error);
      }
    );
  }

  private checkIfBooksOnStock(): void {
    this.booksOnStock = this.commonBookMethodsService.areBooksOnStock(this.bookDetails);
  }

}
