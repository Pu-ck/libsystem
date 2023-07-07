import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonRedirectsService } from '../redirects/common-redirects.service';

@Injectable({
  providedIn: 'root'
})
export class UserEnabledService {

  constructor(
    private http: HttpClient,
    private router: Router,
    private commonRedirectsService: CommonRedirectsService
  ) { }

  public updateUserEnabledStatus(userId: string, reason: string): void {
    const url = `/api/administration/users/${userId}/update-user-enabled-status`
    this.http.put<any>(url, {
      reason: reason,
    }).subscribe(response => {
      console.log(response);
      sessionStorage.setItem('hasUpdatedUserEnableStatus', 'true');
      this.router.navigateByUrl(`/administration/users/${userId}/user-enabled-status/user-enabled-status-updated`);
    }, error => {
      console.log(error);
    }
    );
  }

  public validateIfUserIsEnabled(error: any): void {
    if (error.status === 403 && error.error.message === 'User not enabled') {
      this.commonRedirectsService.logout('accountDisabled');
    }
  }

}
