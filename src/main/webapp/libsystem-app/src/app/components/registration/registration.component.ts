import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonRedirectsService } from '../../services/redirects/common-redirects.service';

@Component({
  selector: 'app-register',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  public model: any = {};
  public usernameTaken: boolean = false;
  public cardNumberTaken: boolean = false;
  public cardNumberNotFound: boolean = false;
  public peselNumberNotAuthenticated: boolean = false;

  constructor(
      private router: Router,
      private http: HttpClient,
      public commonRedirectsService: CommonRedirectsService
  ) { }

  ngOnInit(): void {
  }

  public register(): void {
    const url = '/api/registration';
    this.http.post<any>(url, {
      username: this.model.username,
      password: this.model.password,
      firstName: this.model.firstName,
      lastName: this.model.lastName,
      cardNumber: this.model.cardNumber,
      peselNumber: this.model.peselNumber
    }).subscribe(response => {
      this.usernameTaken = false;
      this.cardNumberTaken = false;
      this.cardNumberNotFound = false;
      this.peselNumberNotAuthenticated = false;
      const token = response.token;
      this.router.navigate(['/registered'], { queryParams: { token: token, username: this.model.username } });
    }, error => {
      if (error.status === 409) {
        if (error.error.message === 'Username already taken') {
          this.usernameTaken = true;
        } else {
          this.usernameTaken = false;
        }
        if (error.error.message === 'Card number already taken') {
          this.cardNumberTaken = true;
        } else {
          this.cardNumberTaken = false;
        }
      }
      if (error.status === 404 && error.error.message === 'Card number not found') {
        this.cardNumberNotFound = true;
      } else {
        this.cardNumberNotFound = false;
      }
      if (error.status === 401 && error.error.message === 'PESEL number not authenticated') {
        this.peselNumberNotAuthenticated = true;
      } else {
        this.peselNumberNotAuthenticated = false;
      }
    });
  }

}