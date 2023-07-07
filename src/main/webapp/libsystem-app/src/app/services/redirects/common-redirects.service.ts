import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CommonRedirectsService {

  constructor(
    private router: Router,
    private http: HttpClient
  ) { }

  public redirectToLoginForm(): void {
    this.router.navigate(['/login']);
  }

  public redirectToUsersList(): void {
    this.router.navigateByUrl('/administration/users');
  }

  public redirectToCardNumbersList(): void {
    this.router.navigateByUrl('/administration/card-numbers');
  }

  public redirectToBorrowedAndOrderedBooks(): void {
    this.router.navigate([`administration/books/`]);
  }

  public logout(logoutType: string): void {
    const url = '/api/logout';
    this.http.post(url, {}).subscribe(() => {
      if (logoutType === 'normal') {
        this.router.navigate(['/login'], { queryParams: { logout: true } });
      }
      if (logoutType === 'accountDisabled') {
        this.router.navigate(['/login'], { queryParams: { disabled: true } });
      }
      sessionStorage.clear();
    });
  }

  public checkSingleVisitPageSessionStorageCondition(sessionStorageItem: string): void {
    const condition = sessionStorage.getItem(sessionStorageItem);
    if (condition !== 'true') {
      this.router.navigateByUrl('/');
    }
    sessionStorage.setItem(sessionStorageItem, 'false');
  }

}
