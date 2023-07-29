import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserEnabledService } from 'src/app/services/user/user-enabled.service';
import { Observer } from 'rxjs';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {

  public model: any = {};
  public passwordDuplicated: boolean = false;
  public oldPasswordNotMatching: boolean = false;
  public newPasswordSet = false;

  constructor(
    private http: HttpClient,
    private userEnabledService: UserEnabledService
  ) { }

  ngOnInit(): void {
  }

  public changePassword(): void {
    const url = '/api/userprofile/change-password';
    const observer: Observer<any> = {
      next: (response) => {
        console.log(response);
        this.newPasswordSet = true;
        this.passwordDuplicated = false;
        this.oldPasswordNotMatching = false;
      },
      error: (error) => {
        this.userEnabledService.validateIfUserIsEnabled(error);
        if (error.status === 409) {
          if (error.error.message === 'New password same as old one') {
            this.passwordDuplicated = true;
            this.oldPasswordNotMatching = false;
            this.newPasswordSet = false;
          }
          if (error.error.message === 'Old password not matching') {
            this.oldPasswordNotMatching = true;
            this.passwordDuplicated = false;
            this.newPasswordSet = false;
          }
        }
      },
      complete: () => {
      },
    };
  
    this.http.put<any>(url, {
      oldPassword: this.model.oldPassword,
      newPassword: this.model.newPassword
    }).subscribe(observer);
  }

}
