import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Observer } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  public model: any = {};
  public sessionID: any = "";
  public userType: any = "";
  public unauthorized: boolean = false;
  public notEnabled: boolean = false;
  public loggedOut: boolean = false;
  public disabled: boolean = false;

  constructor(
    private router: Router,
    private http: HttpClient,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.validateIfUserISLoggedOutOrDisabled();
  }

  public login(): void {
    const url = '/api/login';
    const observer: Observer<any> = {
      next: (response) => {
        this.sessionID = response.sessionID;
        this.userType = response.userType;
        localStorage.setItem('token', this.sessionID);
        localStorage.setItem('userType', this.userType);
        window.location.href = '/';
      },
      error: (error) => {
        if (error.status === 403 && error.error.message === 'User not enabled') {
          this.notEnabled = true;
          this.unauthorized = false;
          this.loggedOut = false;
        }
        if (error.status === 401) {
          this.unauthorized = true;
          this.notEnabled = false;
          this.loggedOut = false;
        }
      },
      complete: () => {
      },
    };
  
    this.http.post<any>(url, {
      username: this.model.username,
      password: this.model.password
    }).subscribe(observer);
  }

  public redirectToRegistrationForm(): void {
    this.router.navigate(['/registration']);
  }

  public redirectToPasswordReminderForm(): void {
    this.router.navigate(['/password-reminder']);
  }

  private validateIfUserISLoggedOutOrDisabled(): void {
    this.route.queryParams.subscribe(params => {
      if (params['logout'] === 'true') {
        this.loggedOut = true;
      }
      if (params['disabled'] === 'true') {
        this.disabled = true;
      }
    });
  }

}