import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

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
    this.http.get<any[]>(url, {}).subscribe(
      response => {
        localStorage.setItem('adminId', response.toString());
      },
      error => {
        console.log(error);
      }
    );
  }

}
