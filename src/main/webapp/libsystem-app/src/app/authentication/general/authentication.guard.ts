import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationGuard implements CanActivate {

  constructor(private router: Router) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    const disabledRoutes = ['/login', '/registration', '/password-reminder', '/registered'];
    if (disabledRoutes.includes(state.url) || state.url.startsWith('/password-reminder/new-password')) {
      if (localStorage.getItem('token')) {
        return this.router.parseUrl('/');
      } else {
        return true;
      }
    }

    let token = localStorage.getItem('token');

    if (!token) {
      return this.router.parseUrl('/login');
    }

    return true;
  }

}