import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observer } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UsertypeService {

  constructor(
    private http: HttpClient
  ) { }

  public validateIfAdminIsLoggedIn(): void {
    const userType = localStorage.getItem('userType');
    const adminId = localStorage.getItem('adminId');
    if (userType === 'ADMIN' && adminId == null) {
      this.setAdminId();
    }
  }

  private setAdminId(): void {
    const url = '/api/administration/users/admin-id';
    const observer: Observer<any> = {
      next: (response) => {
        localStorage.setItem('adminId', response.toString());
      },
      error: (error) => {
        console.log(error);
      },
      complete: () => {
      },
    };

    this.http.get<any[]>(url, {}).subscribe(observer);
  }

}
