import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { CommonRedirectsService } from '../../../services/common-redirects.service';

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

  private verifyToken() {
    const url = '/api/password-reminder/new-password';
    let params = new HttpParams().set('token', this.token);
    this.http.get<any>(url, { params,
    }).subscribe(response => {
      console.log(response);
      this.tokenVerified = true;
    }, error => {
      if (error.status === 404) {
        if (error.error.message === 'Password reminder token expired') {
          this.tokenVerified = false;
          this.tokenExpired = true;
        }
        if (error.error.message === 'Password reminder token not found') {
          this.router.navigate(['/login']);
        }
      }
    }
    );
  }

  public resetPassword() {
    const url = `/api/password-reminder/${this.token}/reset-password`
    this.http.put<any>(url, {
      password: this.model.password
    }).subscribe(response => {
        console.log(response);
        this.passwordReset = true;
    }, error => {
      console.log(error);
    }
    );
  }

}
