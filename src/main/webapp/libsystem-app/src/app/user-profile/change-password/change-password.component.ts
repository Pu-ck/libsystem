import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {

  public model: any = {};
  public passwordDuplicated: boolean = false;
  public newPasswordSet = false;

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
  }

  public changePassword() {
    const url = '/api/userprofile/change-password';
    this.http.put<any>(url, {
      oldPassword: this.model.oldPassword,
      newPassword: this.model.newPassword
    }).subscribe(response => {
        this.newPasswordSet = true;
    }, error => {
      if (error.status === 401) {
        this.passwordDuplicated = true;
      }
    }
    );
  }

}
