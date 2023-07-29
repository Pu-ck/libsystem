import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { UserEnabledService } from 'src/app/services/user/user-enabled.service';
import { PaginationService } from 'src/app/services/pagination/pagination.service';
import { CardNumber } from 'src/app/models/book-properties/card-number';
import { CommonConstants } from 'src/app/utils/common-constants';
import { Observer } from 'rxjs';

@Component({
  selector: 'app-card-numbers',
  templateUrl: './card-numbers.component.html',
  styleUrls: ['./card-numbers.component.css']
})
export class CardNumbersComponent implements OnInit {

  public cardNumbers: CardNumber[] = [];

  public searchType: string = '';
  public searchValue: string = '';

  public currentPage: number = CommonConstants.DEFAULT_PAGE_NUMBER;
  public itemsPerPage: number = CommonConstants.ITEMS_PER_PAGE;

  public cardNumberNotFound: boolean = false;

  constructor(
    private http: HttpClient,
    private router: Router,
    private userEnabledService: UserEnabledService,
    private route: ActivatedRoute,
    public pagination: PaginationService
  ) { }

  ngOnInit(): void {
    this.displayCardNumbersOnInitOrCardNumberOnRedirect();
  }

  public onPageChange(page: number): void {
    this.currentPage = page;
  }

  public getCardNumbers(): void {
    const url = '/api/administration/card-numbers';
    let params = this.getSearchType();
    const observer: Observer<any[]> = {
      next: (response) => {
        this.cardNumbers = response;
        this.cardNumberNotFound = false;
        let queryParams: { [key: string]: string } = {};
        this.setQueryParams(queryParams);
        this.router.navigate(['/administration/card-numbers'], { queryParams });
        this.currentPage = this.cardNumbers.length === 1 ? CommonConstants.DEFAULT_PAGE_NUMBER : this.currentPage;
      },
      error: (error) => {
        this.userEnabledService.validateIfUserIsEnabled(error);
        if (error.status === 404 && error.error.message === 'Card number not found') {
          this.cardNumberNotFound = true;
          this.cardNumbers = [];
          console.log(error);
        }
      },
      complete: () => {
      },
    };

    this.http.get<any[]>(url, { params }).subscribe(observer);
  }

  public redirectToRegisterNewCardNumberForm(): void {
    this.router.navigate(['administration/card-numbers/register-card-number']);
  }

  private displayCardNumbersOnInitOrCardNumberOnRedirect(): void {
    this.route.queryParams.subscribe(params => {
      if (!params['cardNumber']) {
        this.getCardNumbers();
      } else {
        this.getCardNumber(params['cardNumber']);
      }
    });
  }

  private getCardNumber(cardNumber: string): void {
    const url = '/api/administration/card-numbers';
    let params = new HttpParams().set('cardNumber', cardNumber);
    const observer: Observer<any[]> = {
      next: (response) => {
        this.cardNumbers = response;
        this.cardNumberNotFound = false;
      },
      error: (error) => {
        this.userEnabledService.validateIfUserIsEnabled(error);
        if (error.status === 404 && error.error.message === 'Card number not found') {
          this.cardNumberNotFound = true;
          this.cardNumbers = [];
          console.log(error);
        }
      },
      complete: () => {
      },
    };

    this.http.get<any[]>(url, { params }).subscribe(observer);
  }

  private getSearchType(): HttpParams {
    if (this.searchType === 'cardNumberId') {
      return new HttpParams().set('cardNumberId', this.searchValue);
    }
    if (this.searchType === 'cardNumber') {
      return new HttpParams().set('cardNumber', this.searchValue);
    }
    return new HttpParams().set('', '');
  }

  private setQueryParams(queryParams: { [key: string]: string }): void {
    if (this.searchType === 'cardNumberId') {
      queryParams['cardNumberId'] = this.searchValue;
    }
    if (this.searchType === 'cardNumber') {
      queryParams['cardNumber'] = this.searchValue;
    }
  }

}
