import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UsertypeService } from 'src/app/services/user/usertype.service';
import { CommonRedirectsService } from 'src/app/services/redirects/common-redirects.service';

@Component({
  selector: 'app-navigation-menu',
  templateUrl: './navigation-menu.component.html',
  styleUrls: ['./navigation-menu.component.css']
})
export class NavigationMenuComponent implements OnInit {

  public loggedIn: boolean = false;
  public userType: string = '';
  public language: string = localStorage.getItem('language') || 'en';

  constructor(
    private route: ActivatedRoute,
    private userTypeService: UsertypeService,
    public commonRedirectsService: CommonRedirectsService
  ) { }

  ngOnInit(): void {
    this.hideNavbarOnLogout();
    this.checkIfUserIsLoggedIn();
    this.userTypeService.validateIfAdminIsLoggedIn();
    this.userType = localStorage.getItem('userType') || '';
  }

  public setAppLanguage(language: string): void {
      localStorage.setItem('language', language);
      window.location.reload();
  }

  private checkIfUserIsLoggedIn(): void {
    let token = localStorage.getItem('token');
    if (token != null) {
      this.loggedIn = true;
    } else {
      this.loggedIn = false;
    }
  }

  private hideNavbarOnLogout(): void {
    this.route.queryParams.subscribe(params => {
      if (params['logout'] === 'true' || params['disabled'] === 'true') {
        this.loggedIn = false;
      }
    });
  }

}
