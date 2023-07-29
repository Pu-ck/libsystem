import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { UserEnabledService } from 'src/app/services/user/user-enabled.service';
import { CommonRedirectsService } from 'src/app/services/redirects/common-redirects.service';
import { Observer } from 'rxjs';
@Component({
  selector: 'app-user-enabled-status',
  templateUrl: './user-enabled-status.component.html',
  styleUrls: ['./user-enabled-status.component.css']
})
export class UserEnabledStatusComponent implements OnInit {

  public model: any = {};

  public userId: string = '';
  private adminId: string = '';

  public users: any = [];
  public username: string = '';

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
    public userEnabledService: UserEnabledService,
    public commonRedirectsService: CommonRedirectsService
  ) { }

  ngOnInit(): void {
    this.getCurrentUserInformation();
    this.redirectIfAdminIdIsMatchingUserId();
  }

  public updateUserEnabledStatus(): void {
    const url = `/api/administration/users/${this.userId}/update-user-enabled-status`;
    const observer: Observer<any> = {
      next: (response) => {
        console.log(response);
        localStorage.setItem('hasUpdatedUserEnableStatus', 'true');
        this.router.navigateByUrl(`/administration/users/${this.userId}/user-enabled-status/user-enabled-status-updated`);
      },
      error: (error) => {
        this.userEnabledService.validateIfUserIsEnabled(error);
      },
      complete: () => {
      },
    };

    this.http.put<any>(url, {
      reason: this.model.reason,
    }).subscribe(observer);
  }

  private getCurrentUserInformation(): void {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.userId = String(params.get('id')!);
    });

    const url = `api/administration/users?userId=${this.userId}`;
    const observer: Observer<any> = {
      next: (response) => {
        this.users = response;
        this.username = this.users[0].username;
      },
      error: (error) => {
        this.userEnabledService.validateIfUserIsEnabled(error);
        if (error.status === 404 && error.error.message === 'User not found' || error.status === 400) {
          this.router.navigate(['/']);
        }
      },
      complete: () => {
      },
    };

    this.http.get<any>(url, {}).subscribe(observer);
  }

  private redirectIfAdminIdIsMatchingUserId(): void {
    this.adminId = localStorage.getItem('adminId') || '';
    if (this.adminId === this.userId) {
      this.router.navigateByUrl('/');
    }
  }

}
