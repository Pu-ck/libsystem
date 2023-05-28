import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonBookMethodsService } from '../../../../services/book/common-book-methods.service';
import affiliates from 'src/config/affiliates.json';

@Component({
  selector: 'app-borrow-book',
  templateUrl: './borrow-book.component.html',
  styleUrls: ['./borrow-book.component.css']
})
export class BorrowBookComponent implements OnInit {

  public model: any = {};
  public affiliateOptions: any[] = affiliates;
  public affiliateOptionsForBook: any[] = [];
  public bookDetails: any;
  public maxQuantity: number = 0;
  public title: string = ';';

  private id: string = '';

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
    public commonBookMethods: CommonBookMethodsService
  ) { }

  ngOnInit(): void {
    this.validateBookId();
  }

  private validateBookId(): void {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.id = params.get('id')!;
    });

    const url = `api/books/${this.id}`;
    this.http.get<any>(url, { }).subscribe(response => {
      this.title = response.title;
      this.bookDetails = response;
      this.redirectIfNoBooksOnStock();
      this.chooseAffiliateOptionsForBook();
    }, error => {
        if (error.status === 404 && error.error.message === 'Book not found') {
            console.log(error);
            this.router.navigate(['/']);
        }
      }
    );
  }

  public borrow(): void {
  }

  public setQunatityRange(start: number, end: number, step: number): number[] {
    const length = Math.floor((end - start) / step) + 1;
    return Array(length).fill(0).map((_, index) => start + index * step);
  }

  private redirectIfNoBooksOnStock(): void {
    if(!this.commonBookMethods.areBooksOnStock(this.bookDetails)) {
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

  public specifyQuantityForChosenAffiliate(): void{
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

}
