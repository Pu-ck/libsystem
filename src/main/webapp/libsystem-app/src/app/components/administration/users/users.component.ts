import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { UserEnabledService } from 'src/app/services/user/user-enabled.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  public users: any[] = [];
  public userId: string = '';
  public adminId: string = '';

  public searchType: string = '';
  public searchValue: string = '';

  public userNotFound: boolean = false;
  public showEnabled: boolean = false;
  public showDisabled = false;
  public showAll: boolean = true;

  constructor(
    private http: HttpClient,
    private router: Router,
    private userEnabledService: UserEnabledService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.adminId = sessionStorage.getItem('adminId') || '';
    this.route.queryParams.subscribe(params => {
      if (!params['userId']) {
        this.getUsers();
      } else {
        this.getUser(params['userId']);
      }
    });
  }

  public getUsers(): void {
    const url = '/api/administration/users';
    let params = this.getSearchType();
    this.http.get<any[]>(url, { params }).subscribe(
      response => {
        this.users = response;
        this.userNotFound = false;
        let queryParams: { [key: string]: string } = {};
        this.setQueryParams(queryParams);
        this.router.navigate(['/administration/users'], { queryParams });
      },
      error => {
        this.userEnabledService.validateIfUserIsEnabled(error);
        if (error.status === 404 && error.error.message === 'User not found') {
          this.userNotFound = true;
          this.users = [];
        }
      }
    );
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

  public redirectToUserUpdateEnabledStatusForm(id: string, status: boolean): void {
    if (status) {
      this.router.navigate([`administration/users/${id}/user-enabled-status`]);
    } else {
      this.userEnabledService.updateUserEnabledStatus(id, '');
    }
  }

  private getUser(userId: string): void {
    const url = '/api/administration/users';
    let params = new HttpParams().set('userId', userId);
    this.http.get<any[]>(url, { params }).subscribe(
      response => {
        this.users = response;
        this.userNotFound = false;
      },
      error => {
        this.userEnabledService.validateIfUserIsEnabled(error);
        if (error.status === 404 && error.error.message === 'User not found') {
          this.userNotFound = true;
          this.users = [];
        }
      }
    );
  }

  private getSearchType(): HttpParams {
    if (this.searchType === 'userId') {
      return new HttpParams().set('userId', this.searchValue);
    }
    if (this.searchType === 'username') {
      return new HttpParams().set('username', this.searchValue);
    }
    if (this.searchType === 'cardNumber') {
      return new HttpParams().set('cardNumber', this.searchValue);
    }
    return new HttpParams().set('', '');
  }

  private setQueryParams(queryParams: { [key: string]: string }): void {
    if (this.searchType === 'userId') {
      queryParams['userId'] = this.searchValue;
    }
    if (this.searchType === 'username') {
      queryParams['username'] = this.searchValue;
    }
    if (this.searchType === 'cardNumber') {
      queryParams['cardNumber'] = this.searchValue;
    }
  }

}
