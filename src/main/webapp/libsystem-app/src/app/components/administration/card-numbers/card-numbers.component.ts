import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { UserEnabledService } from 'src/app/services/user/user-enabled.service';

@Component({
  selector: 'app-card-numbers',
  templateUrl: './card-numbers.component.html',
  styleUrls: ['./card-numbers.component.css']
})
export class CardNumbersComponent implements OnInit {

  public cardNumbers: any[] = [];

  public searchType: string = '';
  public searchValue: string = '';

  public cardNumberNotFound: boolean = false;
  
  constructor(
    private http: HttpClient,
    private router: Router,
    private userEnabledService: UserEnabledService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if (!params['cardNumber']) {
        this.getCardNumbers();
      } else {
        this.getCardNumber(params['cardNumber']);
      }
    });
  }

  public getCardNumbers(): void {
    const url = '/api/administration/card-numbers';
    let params = this.getSearchType();
    this.http.get<any[]>(url, {params}).subscribe(
      response => {
        this.cardNumbers = response;
        this.cardNumberNotFound = false;
        let queryParams: {[key: string]: string} = {};
        this.setQueryParams(queryParams);
        this.router.navigate(['/administration/card-numbers'], { queryParams });
      },
      error => {
        this.userEnabledService.validateIfUserIsEnabled(error);
        if (error.status === 404 && error.error.message === 'Card number not found') {
          this.cardNumberNotFound = true;
          this.cardNumbers = [];
          console.log(error);
        }
      }
    );
  }

  public redirectToRegisterNewCardNumberForm(): void {
    this.router.navigate(['administration/card-numbers/register-card-number']);
  }

  private getCardNumber(cardNumber: string): void {
    const url = '/api/administration/card-numbers';
    let params = new HttpParams().set('cardNumber', cardNumber);
    this.http.get<any[]>(url, {params}).subscribe(
      response => {
        this.cardNumbers = response;
        this.cardNumberNotFound = false;
      },
      error => {
        this.userEnabledService.validateIfUserIsEnabled(error);
        if (error.status === 404 && error.error.message === 'Card number not found') {
          this.cardNumberNotFound = true;
          this.cardNumbers = [];
          console.log(error);
        }
      }
    );
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

  private setQueryParams(queryParams: {[key: string]: string}): void {
    if (this.searchType === 'cardNumberId') {
      queryParams['cardNumberId'] = this.searchValue;
    }
    if (this.searchType === 'cardNumber') {
      queryParams['cardNumber'] = this.searchValue;
    }
  }

}
