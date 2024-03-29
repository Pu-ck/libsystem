import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { UserEnabledService } from 'src/app/services/user/user-enabled.service';
import { PaginationService } from 'src/app/services/pagination/pagination.service';
import { User } from 'src/app/models/users/user';
import { CommonConstants } from 'src/app/utils/common-constants';
import { Observer } from 'rxjs';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  public currentPage: number = CommonConstants.DEFAULT_PAGE_NUMBER;
  public itemsPerPage: number = CommonConstants.ITEMS_PER_PAGE;

  public users: User[] = [];
  public userId: string = localStorage.getItem('enabledUserId') || '';
  public adminId: string = '';

  public searchType: string = '';
  public searchValue: string = '';

  public userNotFound: boolean = false;
  public showEnabled: boolean = false;
  public showDisabled: boolean = false;
  public userEnabled: boolean = false;
  public showAll: boolean = true;

  public display = CommonConstants.MODAL_DISPLAY_HIDE;

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
    this.display = CommonConstants.MODAL_DISPLAY_SHOW;
    this.userEnabled = false;
  }

  public onCloseHandled(): void {
    this.display = CommonConstants.MODAL_DISPLAY_HIDE;
  }

  public getUsers(status: string): void {
    this.userEnabled = false;
    const url = '/api/administration/users';
    let params = this.getSearchType();
    const observer: Observer<any[]> = {
      next: (response) => {
        if (status === 'All') {
          this.users = response;
        }
        if (status === 'Enabled') {
          this.users = response.filter(users => users.enabled === true);
        }
        if (status === 'Disabled') {
          this.users = response.filter(users => users.enabled === false);
        }

        let queryParams: { [key: string]: string } = {};
        this.setQueryParams(queryParams);
        this.router.navigate(['/administration/users'], { queryParams });
        this.userNotFound = this.users.length === 0;
        this.currentPage = this.users.length === 1 ? CommonConstants.DEFAULT_PAGE_NUMBER : this.currentPage;
      },
      error: (error) => {
        this.userEnabledService.validateIfUserIsEnabled(error);
        if (error.status === 404 && error.error.message === 'User not found') {
          this.userNotFound = true;
          this.users = [];
        }
      },
      complete: () => {
      },
    };

    this.http.get<any[]>(url, { params }).subscribe(observer);
  }


  public setDisplayedStatus(status: string): void {
    this.currentPage = CommonConstants.DEFAULT_PAGE_NUMBER;
    this.getUsers(status);
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
        this.getUsers('All');
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
    const observer: Observer<any[]> = {
      next: (response) => {
        this.users = response;
        this.userNotFound = false;
      },
      error: (error) => {
        this.userEnabledService.validateIfUserIsEnabled(error);
        if (error.status === 404 && error.error.message === 'User not found') {
          this.userNotFound = true;
          this.users = [];
        }
      },
      complete: () => {
      },
    };

    this.http.get<any[]>(url, { params }).subscribe(observer);
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
