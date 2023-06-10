import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  public users: any[] = [];
  public userId: string = '';
  public userNotFound: boolean = false;
  public showEnabled: boolean = false;
  public showDisabled = false;
  public showAll: boolean = true;

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.userId = params['userId'] || '';
      this.getUsers();
    });
  }

  public getUsers(): void {
    const url = '/api/administration/users';
    let params = new HttpParams().set('userId', this.userId);
    this.http.get<any[]>(url, {params}).subscribe(
      response => {
        this.users = response;
        this.userNotFound = false;
        let queryParams: {[key: string]: string} = {};
        this.setQueryParams(queryParams);
        this.router.navigate(['/administration/users'], { queryParams });
      },
      error => {
        if (error.status === 404 && error.error.message === 'User not found') {
          this.userNotFound = true;
          this.users = [];
          console.log(error);
        }
      }
    );
  }

  public redirectToUserUpdateEnabledStatusForm(id: number): void {
    this.router.navigate([`administration/users/${id}/user-enabled-status`]);
  }

  private setQueryParams(queryParams: {[key: string]: string}): void {
    if (this.userId) {
      queryParams['userId'] = this.userId;
    }
  }

  public setDisplayedStatus(status: string): void {
    if (status === 'Enabled') {
      this.showEnabled = true;
      this.showDisabled = false;
      this.showAll = false;
    } else if (status === 'Disabled') {
      this.showDisabled = true;
      this.showEnabled = false;
      this.showAll = false;
    } else if (status === 'All') {
      this.showAll = true;
      this.showEnabled = false;
      this.showDisabled = false;
    } 
  }

}
