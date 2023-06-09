import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UsertypeService {

  constructor() { }

  public validateIfAdminIsLoggedIn(): boolean {
    const userType = sessionStorage.getItem('userType');
    if (userType === 'ADMIN') {
      return true;
    }
    return false;
  }

}
