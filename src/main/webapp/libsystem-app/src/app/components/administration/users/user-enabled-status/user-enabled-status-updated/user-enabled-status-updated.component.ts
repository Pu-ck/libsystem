import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonRedirectsService } from 'src/app/services/redirects/common-redirects.service';

@Component({
  selector: 'app-user-enabled-status-updated',
  templateUrl: './user-enabled-status-updated.component.html',
  styleUrls: ['./user-enabled-status-updated.component.css']
})
export class UserEnabledStatusUpdatedComponent implements OnInit {

  public userEnabledStatusUpdated: boolean = false;

  constructor(
    private router: Router,
    public commonRedirectsService: CommonRedirectsService
  ) { }

  ngOnInit(): void {
    this.checkIfUserEnabledStatusHasBeenUpdated();
    this.userEnabledStatusUpdated = true;
  }

  public redirectToUsersList(): void {
    this.router.navigateByUrl('/administration/users');
  }

  private checkIfUserEnabledStatusHasBeenUpdated(): void {
    const hasUpdatedUserEnableStatus = localStorage.getItem('hasUpdatedUserEnableStatus');
    if (hasUpdatedUserEnableStatus !== 'true') {
      this.router.navigateByUrl('/');
    }
    localStorage.setItem('hasUpdatedUserEnableStatus', 'false');
  } 

}
