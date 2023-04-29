import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  model: any = {};
  sessionID: any = "";
  loginError = false;

  constructor(
      private router: Router,
      private http: HttpClient
  ) { }

  ngOnInit(): void {
  }

  login() {
    let url = '/api/login';
    this.http.post<any>(url, {
      username: this.model.username,
      password: this.model.password
    }).subscribe(response => {
        this.sessionID = response.sessionID;  
        sessionStorage.setItem(
          'token',
          this.sessionID
        );
        this.router.navigate(['']);
    }, error => {
      if (error.status === 401) {
        this.loginError = true;
      }
    }
    );
  }

  redirectToRegistrationForm() {
    this.router.navigate(['/registration']);
  }
  
}