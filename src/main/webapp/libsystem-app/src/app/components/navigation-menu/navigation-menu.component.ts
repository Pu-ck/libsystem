import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { UsertypeService } from '../../services/usertype/usertype.service';

@Component({
  selector: 'app-navigation-menu',
  templateUrl: './navigation-menu.component.html',
  styleUrls: ['./navigation-menu.component.css']
})
export class NavigationMenuComponent implements OnInit {
  
  public loggedIn: boolean = false;
  public loggedAsAdmin: boolean = false;

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
    private userTypeService: UsertypeService
  ) { }

  ngOnInit(): void {
    this.hideNavbarOnLogout();
    this.checkIfUserIsLoggedIn();
    this.loggedAsAdmin = this.userTypeService.validateIfAdminIsLoggedIn();
  }

  public logout(): void {
    const url = '/api/logout';
    this.http.post(url, {}).subscribe(() => {
      console.log('Logout successful');
      this.router.navigate(['/login'], { queryParams: { logout: true } });  
      sessionStorage.clear();
    });
  }

  private checkIfUserIsLoggedIn(): void {
    let token = sessionStorage.getItem('token');
    if (token != null) {
      this.loggedIn = true;
    } else {
      this.loggedIn = false;
    }
  }

  private hideNavbarOnLogout(): void {
    this.route.queryParams.subscribe(params => {
      if (params['logout'] === 'true') {
        this.loggedIn = false;
      }
    });
  }

}
