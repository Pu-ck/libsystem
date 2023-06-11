import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UsertypeService {

  private adminId: string = '';

  constructor(
    private http: HttpClient
  ) { }

  public validateIfAdminIsLoggedIn(): boolean {
    const userType = sessionStorage.getItem('userType');
    if (userType === 'ADMIN') {
      this.setAdminId();
      return true;
    }
    return false;
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
