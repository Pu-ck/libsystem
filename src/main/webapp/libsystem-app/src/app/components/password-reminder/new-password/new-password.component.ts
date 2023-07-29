import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { CommonRedirectsService } from 'src/app/services/redirects/common-redirects.service';
import { Observer } from 'rxjs';

@Component({
  selector: 'app-new-password',
  templateUrl: './new-password.component.html',
  styleUrls: ['./new-password.component.css']
})
export class NewPasswordComponent implements OnInit {

  public model: any = {};
  public token: string = '';
  public tokenVerified: boolean = false;
  public passwordReset: boolean = false;
  public tokenExpired: boolean = false;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient,
    public commonRedirectsService: CommonRedirectsService
  ) { }

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token')!;
    this.verifyToken();
  }

  private verifyToken(): void {
    const url = '/api/password-reminder/new-password';
    const params = new HttpParams().set('token', this.token);
    const observer: Observer<any> = {
      next: (response) => {
        console.log(response);
        this.tokenVerified = true;
      },
      error: (error) => {
        if (error.status === 404) {
          if (error.error.message === 'Password reminder token expired') {
            this.tokenVerified = false;
            this.tokenExpired = true;
          }
          if (error.error.message === 'Password reminder token not found') {
            this.router.navigate(['/login']);
          }
        }
      },
      complete: () => {
      },
    };

    this.http.get<any>(url, { params }).subscribe(observer);
  }

  public resetPassword(): void {
    const url = `/api/password-reminder/${this.token}/reset-password`;
    const observer: Observer<any> = {
      next: (response) => {
        console.log(response);
        this.passwordReset = true;
      },
      error: (error) => {
        console.log(error);
      },
      complete: () => {
      },
    };

    this.http.put<any>(url, {
      password: this.model.password
    }).subscribe(observer);
  }

}
