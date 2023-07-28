import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AdministratorGuard  {

  constructor(private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean | UrlTree {
    const userType = localStorage.getItem('userType');
    if (userType === 'ADMIN') {
      return true;
    } else {
      return this.router.parseUrl('/');
    }
  }
  
}
