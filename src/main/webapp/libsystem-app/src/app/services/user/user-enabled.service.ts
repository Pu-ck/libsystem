import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonRedirectsService } from '../redirects/common-redirects.service';
import { Observer } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserEnabledService {

  constructor(
    private http: HttpClient,
    private router: Router,
    private commonRedirectsService: CommonRedirectsService
  ) { }

  public enableUser(userId: string, reason: string): void {
    const url = `/api/administration/users/${userId}/update-user-enabled-status`;
    const observer: Observer<any> = {
      next: (response) => {
        console.log(response);
        localStorage.setItem('hasEnabledUser', 'true');
        localStorage.setItem('enabledUserId', userId);
        window.location.reload();
      },
      error: (error) => {
        console.log(error);
      },
      complete: () => {
      },
    };
  
    this.http.put<any>(url, {
      reason: reason,
    }).subscribe(observer);
  }

  public disableUser(userId: string, reason: string): void {
    const url = `/api/administration/users/${userId}/update-user-enabled-status`;
    const observer: Observer<any> = {
      next: (response) => {
        console.log(response);
        localStorage.setItem('hasDisabledUser', 'true');
        this.router.navigateByUrl(`/administration/users/${userId}/user-enabled-status/user-enabled-status-updated`);
      },
      error: (error) => {
        console.log(error);
      },
      complete: () => {
      },
    };
  
    this.http.put<any>(url, {
      reason: reason,
    }).subscribe(observer);
  }

  public validateIfUserIsEnabled(error: any): void {
    if (error.status === 403 && error.error.message === 'User not enabled') {
      this.commonRedirectsService.logout('accountDisabled');
    }
  }

}
