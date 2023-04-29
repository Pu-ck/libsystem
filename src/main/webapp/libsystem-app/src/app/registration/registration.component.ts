import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  model: any = {};
  usernameTaken = false;
  cardNumberTaken = false;

  constructor(
      private router: Router,
      private http: HttpClient
  ) { }

  ngOnInit(): void {
  }

  register() {
    let url = '/api/registration';
    this.http.post<any>(url, {
      username: this.model.username,
      password: this.model.password,
      firstName: this.model.firstName,
      lastName: this.model.lastName,
      cardNumber: this.model.cardNumber
    }).subscribe(response => {
      this.usernameTaken = false;
      this.cardNumberTaken = false;
      const token = response.token;
      this.router.navigate(['/registered'], { queryParams: { token: token, username: this.model.username } });
    }, error => {
      if (error.status === 409 && error.error.message === "Username already taken") {
        this.usernameTaken = true;
      } else {
        this.usernameTaken = false;
      }
      if (error.status === 409 && error.error.message === "Card number already taken") {
        this.cardNumberTaken = true;
      } else {
        this.cardNumberTaken = false;
      }
    });
  }

  redirectToLoginForm() {
    this.router.navigate(['/login']);
  }

}