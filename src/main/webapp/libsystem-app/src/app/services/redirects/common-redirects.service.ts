import { Injectable } from '@angular/core';
import { Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class CommonRedirectsService {

  constructor(private router: Router) { }

  public redirectToLoginForm() {
    this.router.navigate(['/login']);
  }

}
