import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { CommonRedirectsService } from 'src/app/services/redirects/common-redirects.service';

@Component({
  selector: 'app-user-enabled-status-updated',
  templateUrl: './user-enabled-status-updated.component.html',
  styleUrls: ['./user-enabled-status-updated.component.css']
})
export class UserEnabledStatusUpdatedComponent implements OnInit {

  public userEnabledStatusUpdated: boolean = false;
  public userId: string = '';

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    public commonRedirectsService: CommonRedirectsService
  ) { }

  ngOnInit(): void {
    this.checkIfUserEnabledStatusHasBeenUpdated();
    this.userEnabledStatusUpdated = true;
    this.setUserId();
  }

  public redirectToUsersList(): void {
    this.router.navigateByUrl('/administration/users');
  }

  private checkIfUserEnabledStatusHasBeenUpdated(): void {
    const hasUpdatedUserEnableStatus = sessionStorage.getItem('hasUpdatedUserEnableStatus');
    if (hasUpdatedUserEnableStatus !== 'true') {
      this.router.navigateByUrl('/');
    }
    sessionStorage.setItem('hasUpdatedUserEnableStatus', 'false');
  } 

  private setUserId(): void {
    this.route.paramMap.subscribe(params => {
      this.userId = params.get('id') || '';
    });
  }

}
