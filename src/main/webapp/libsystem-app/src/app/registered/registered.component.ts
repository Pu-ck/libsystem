import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';

@Component({
  selector: 'app-registered',
  templateUrl: './registered.component.html',
  styleUrls: ['./registered.component.css']
})
export class RegisteredComponent implements OnInit {

  public token: string = '';
  public username: string = '';
  public tokenVeryfied: boolean = false;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient
    ) { }

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token')!;
    this.username = this.route.snapshot.queryParamMap.get('username')!;
    this.verifyToken();
  }

  public redirectToLoginForm() {
    this.router.navigate(['/login']);
  }

  private verifyToken() {
    const url = '/api/registered';
    let params = new HttpParams().set('token', this.token).set('username', this.username);
    this.http.get<any>(url, { params,
    }).subscribe(response => {
      console.log(response);
      this.tokenVeryfied = true;
    }, error => {
      console.log(error);
      this.router.navigate(['/login']);
    }
    );

  }

}
