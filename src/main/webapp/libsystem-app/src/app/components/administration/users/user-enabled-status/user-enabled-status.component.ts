import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { UserEnabledService } from 'src/app/services/user/user-enabled.service';
import { CommonRedirectsService } from 'src/app/services/redirects/common-redirects.service';
@Component({
  selector: 'app-user-enabled-status',
  templateUrl: './user-enabled-status.component.html',
  styleUrls: ['./user-enabled-status.component.css']
})
export class UserEnabledStatusComponent implements OnInit {

  public model: any = {};
  public userId: number = 0;
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
  }

  public updateUserEnabledStatus(): void {
    const url = `/api/administration/users/${this.userId}/update-user-enabled-status`
    this.http.put<any>(url, { reason: this.model.reason,
    }).subscribe(response => {
      console.log(response);
      sessionStorage.setItem('hasUpdatedUserEnableStatus', 'true');
      this.router.navigateByUrl(`/administration/users/${this.userId}/user-enabled-status/user-enabled-status-updated`);
    }, error => {
      this.userEnabledService.validateIfUserIsEnabled(error);
    }
    );
  }

  private getCurrentUserInformation(): void {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.userId = Number(params.get('id')!);
    });
    const url = `api/administration/users?userId=${this.userId}`;
    this.http.get<any>(url, { }).subscribe(response => {
      this.users = response;
      this.username = this.users[0].username;
      console.log(response);
    }, error => {
        this.userEnabledService.validateIfUserIsEnabled(error);
        if (error.status === 404 && error.error.message === 'User not found') {
            console.log(error);
            this.router.navigate(['/']);
        }
      }
    );
  }

}
