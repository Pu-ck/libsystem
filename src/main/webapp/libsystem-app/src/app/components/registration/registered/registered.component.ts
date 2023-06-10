import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router} from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { CommonRedirectsService } from '../../../services/redirects/common-redirects.service';

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
    let params = new HttpParams().set('token', this.token).set('username', this.username);
    this.http.get<any>(url, { params,
    }).subscribe(response => {
      console.log(response);
      this.tokenVerified = true;
    }, error => {
      console.log(error);
      this.router.navigate(['/login']);
    }
    );
  }

}
