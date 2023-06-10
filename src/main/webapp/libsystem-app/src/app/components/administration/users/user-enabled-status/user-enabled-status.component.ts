import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

@Component({
  selector: 'app-user-enabled-status',
  templateUrl: './user-enabled-status.component.html',
  styleUrls: ['./user-enabled-status.component.css']
})
export class UserEnabledStatusComponent implements OnInit {

  public model: any = {};
  public userId: string = '';
  public users: any = [];
  public updateType: string = '';
  public username: string = '';

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
  ) { }

  ngOnInit(): void {
    this.getCurrentUserInformation();
  }

  public updateUserEnabledStatus(): void {
    const url = `/api/administration/users/${this.userId}/update-user-enabled-status`
    this.http.put<any>(url, { reason: this.model.reason,
    }).subscribe(response => {
      console.log(response);
      localStorage.setItem('hasUpdatedUserEnableStatus', 'true');
      this.router.navigateByUrl(`/administration/users/${this.userId}/user-enabled-status/user-enabled-status-updated`);
    }, error => {
      console.log(error);
    }
    );
  }

  private getCurrentUserInformation(): void {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.userId = params.get('id')!;
    });
    const url = `api/administration/users?userId=${this.userId}`;
    this.http.get<any>(url, { }).subscribe(response => {
      this.users = response;
      this.updateType = this.users[0].enabled ? 'Disable' : 'Enable';
      this.username = this.users[0].username;
      console.log(response);
    }, error => {
        if (error.status === 404 && error.error.message === 'User not found') {
            console.log(error);
            this.router.navigate(['/']);
        }
      }
    );
  }

}
