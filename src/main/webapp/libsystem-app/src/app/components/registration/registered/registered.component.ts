import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { CommonRedirectsService } from 'src/app/services/redirects/common-redirects.service';
import { Observer } from 'rxjs';

@Component({
  selector: 'app-registered',
  templateUrl: './registered.component.html',
  styleUrls: ['./registered.component.css']
})
export class RegisteredComponent implements OnInit {

  public token: string = '';
  public username: string = '';
  public tokenVerified: boolean = false;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient,
    public commonRedirectsService: CommonRedirectsService
  ) { }

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token')!;
    this.username = this.route.snapshot.queryParamMap.get('username')!;
    this.verifyToken();
  }

  private verifyToken(): void {
    const url = '/api/registered';
    const params = new HttpParams().set('token', this.token).set('username', this.username);
    const observer: Observer<any> = {
      next: (response) => {
        console.log(response);
        this.tokenVerified = true;
      },
      error: (error) => {
        console.log(error);
        this.router.navigate(['/login']);
      },
      complete: () => {
      },
    };
  
    this.http.get<any>(url, { params }).subscribe(observer);
  }

}
