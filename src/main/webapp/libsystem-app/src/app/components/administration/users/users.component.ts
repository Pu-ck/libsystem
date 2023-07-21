import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { UserEnabledService } from 'src/app/services/user/user-enabled.service';
import { PaginationService } from 'src/app/services/pagination/pagination.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  public currentPage: number = 1;
  public itemsPerPage: number = 20;

  public users: any[] = [];
  public userId: string = localStorage.getItem('enabledUserId') || '';
  public adminId: string = '';

  public searchType: string = '';
  public searchValue: string = '';

  public userNotFound: boolean = false;
  public showEnabled: boolean = false;
  public showDisabled: boolean = false;
  public userEnabled: boolean = false;
  public showAll: boolean = true;

  public display = "none";

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
    public userEnabledService: UserEnabledService,
    public pagination: PaginationService
  ) { }

  ngOnInit(): void {
    this.setAdminId();
    this.displayUsersOnInitOrUserOnRedirect();
    this.checkIfUserHasBeenEnabled();
  }

  public onPageChange(page: number): void {
    this.currentPage = page;
    this.userEnabled = false;
  }

  public openModal(): void {
    this.display = "block";
    this.userEnabled = false;
  }

  public onCloseHandled(): void {
    this.display = "none";
  }

  public getUsers(): void {
    this.userEnabled = false;
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
      this.userEnabled = false;
    } else if (status === 'Disabled') {
      this.showDisabled = true;
      this.showEnabled = false;
      this.showAll = false;
      this.userEnabled = false;
    } else if (status === 'All') {
      this.showAll = true;
      this.showEnabled = false;
      this.showDisabled = false;
      this.userEnabled = false;
    }
  }

  public redirectToUserUpdateEnabledStatusForm(id: string, status: boolean): void {
    if (status) {
      this.router.navigate([`administration/users/${id}/user-enabled-status`]);
    } else {
      this.userId = id;
      this.openModal();
    }
  }

  private checkIfUserHasBeenEnabled(): void {
    if (localStorage.getItem('hasEnabledUser') === 'true') {
      this.userEnabled = true;
      localStorage.setItem('hasEnabledUser', 'false');
    }
  }

  private displayUsersOnInitOrUserOnRedirect(): void {
    this.route.queryParams.subscribe(params => {
      if (!params['userId']) {
        this.getUsers();
      } else {
        this.getUser(params['userId']);
      }
    });
  }

  private setAdminId(): void {
    this.adminId = localStorage.getItem('adminId') || '';
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
