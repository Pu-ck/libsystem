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

  private id: string = '';
  public bookDetails: any;
  public booksOnStock: boolean = true;

  constructor(
    private http: HttpClient, 
    private router: Router,
    private route: ActivatedRoute,
    public commonBookMethodsService: CommonBookMethodsService
  ) { }

  ngOnInit(): void {
    this.getBookDetails();
  }

  private getBookDetails(): void {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.id = params.get('id')!;
    });

    const url = `api/books/${this.id}`;
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

  public redirectToOrderForm(): void {
    this.router.navigate([`/books/${this.id}/borrow-book`]);
  }

  private checkIfBooksOnStock(): void {
    this.booksOnStock = this.commonBookMethodsService.areBooksOnStock(this.bookDetails);
  }

}
