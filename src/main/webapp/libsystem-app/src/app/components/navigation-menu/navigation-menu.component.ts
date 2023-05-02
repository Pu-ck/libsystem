import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-navigation-menu',
  templateUrl: './navigation-menu.component.html',
  styleUrls: ['./navigation-menu.component.css']
})
export class NavigationMenuComponent implements OnInit {
  
  public loggedIn: boolean = false;

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.hideNavbarOnLogout();
    this.checkIfUserIsLoggedIn();
  }

  public logout() {
    this.http.post('/api/logout', {}).subscribe(() => {
      console.log('Logout successful');
      this.router.navigate(['/login'], { queryParams: { logout: true } });  
      sessionStorage.clear();
    });
  }

  private checkIfUserIsLoggedIn() {
    let token = sessionStorage.getItem('token');
    if (token != null) {
      this.loggedIn = true;
    } else {
      this.loggedIn = false;
    }
  }

  private hideNavbarOnLogout() {
    this.route.queryParams.subscribe(params => {
      if (params['logout'] === 'true') {
        this.loggedIn = false;
      }
    });
  }

}
