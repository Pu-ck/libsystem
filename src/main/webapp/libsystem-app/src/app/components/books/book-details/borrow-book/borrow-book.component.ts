import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonBookMethodsService } from 'src/app/services/book/common-book-methods.service';
import { UserEnabledService } from 'src/app/services/user/user-enabled.service';
import affiliates from 'src/config/affiliates.json';
import { BookDetails } from 'src/app/models/books/book-details';
import { CommonRedirectsService } from 'src/app/services/redirects/common-redirects.service';
import { Observer } from 'rxjs';

@Component({
  selector: 'app-borrow-book',
  templateUrl: './borrow-book.component.html',
  styleUrls: ['./borrow-book.component.css']
})
export class BorrowBookComponent implements OnInit {

  public model: any = {};
  public affiliateOptions: any[] = affiliates;
  public affiliateOptionsForBook: any[] = [];
  public bookDetails!: BookDetails;
  public maxQuantity: number = 0;
  public title: string = '';

  public bookOutOfStock: boolean = false;
  public invalidCardNumber: boolean = false;
  public tooManyBooks: boolean = false;

  private bookId: string = '';

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
    private userEnabledService: UserEnabledService,
    public commonBookMethods: CommonBookMethodsService,
    public commonRedirectsService: CommonRedirectsService
  ) { }

  ngOnInit(): void {
    this.getCurrentBookInformation();
  }

  public borrow(): void {
    const url = `/api/books/${this.bookId}/borrow-book`;
    const observer: Observer<any> = {
      next: (response) => {
        console.log(response);
        localStorage.setItem('hasBorrowedBook', 'true');
        this.router.navigateByUrl(`/books/${this.bookId}/borrow-book/borrowed`);
      },
      error: (error) => {
        this.userEnabledService.validateIfUserIsEnabled(error);
        if (error.status === 404 && error.error.message === 'Book out of stock') {
          this.bookOutOfStock = true;
          this.invalidCardNumber = false;
          this.tooManyBooks = false;
        }
        if (error.status === 400) {
          if (error.error.message === 'Card number not authenticated') {
            this.invalidCardNumber = true;
            this.bookOutOfStock = false;
            this.tooManyBooks = false;
          }
          if (error.error.message === 'Too many books borrowed') {
            this.tooManyBooks = true;
            this.invalidCardNumber = false;
            this.bookOutOfStock = false;
          }
        }
      },
      complete: () => {
      },
    };
  
    this.http.put<any>(url, {
      cardNumber: this.model.cardNumber,
      quantity: this.model.quantity,
      affiliate: this.model.affiliate
    }).subscribe(observer);
  }

  public setQuantityRange(start: number, end: number, step: number): number[] {
    const length = Math.floor((end - start) / step) + 1;
    return Array(length).fill(0).map((_, index) => start + index * step);
  }

  public specifyQuantityForChosenAffiliate(): void {
    for (let affiliate of this.bookDetails.affiliates) {
      if (affiliate.name === this.model.affiliate) {
        for (let affiliateBook of this.bookDetails.affiliateBooks) {
          if (affiliateBook.affiliateId === affiliate.id) {
            this.maxQuantity = affiliateBook.currentQuantity;
          }
        }
      }
    }
  }

  private getCurrentBookInformation(): void {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.bookId = params.get('id')!;
    });

    const url = `api/books/${this.bookId}`;
    const observer: Observer<any> = {
      next: (response) => {
        this.title = response.title;
        this.bookDetails = response;
        this.redirectIfNoBooksOnStock();
        this.chooseAffiliateOptionsForBook();
      },
      error: (error) => {
        if (error.status === 404 && error.error.message === 'Book not found' || error.status === 400) {
          this.router.navigate(['/']);
        }
      },
      complete: () => {
      },
    };

    this.http.get<any>(url, {}).subscribe(observer);
  }

  private redirectIfNoBooksOnStock(): void {
    if (!this.commonBookMethods.areBooksOnStock(this.bookDetails)) {
      this.router.navigate(['/']);
    }
  }

  private chooseAffiliateOptionsForBook(): void {
    for (let affiliate of this.bookDetails.affiliates) {
      for (let affiliateOption of this.affiliateOptions) {
        if (affiliate.name === affiliateOption.label) {
          for (let affiliateBook of this.bookDetails.affiliateBooks) {
            if (affiliateBook.affiliateId === affiliate.id && affiliateBook.currentQuantity > 0) {
              this.affiliateOptionsForBook.push(affiliateOption);
            }
          }
        }
      }
    }
  }

}
