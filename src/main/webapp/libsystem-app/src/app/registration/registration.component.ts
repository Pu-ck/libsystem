import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-register',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  model: any = {};

  constructor(
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
    }).subscribe();
}
}