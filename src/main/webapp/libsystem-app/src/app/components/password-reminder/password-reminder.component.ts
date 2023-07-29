import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonRedirectsService } from 'src/app/services/redirects/common-redirects.service';
import { Observer } from 'rxjs';

@Component({
  selector: 'app-password-reminder',
  templateUrl: './password-reminder.component.html',
  styleUrls: ['./password-reminder.component.css']
})
export class PasswordReminderComponent implements OnInit {

  public model: any = {};
  public authenticationError: boolean = false;
  public authenticatedSuccessfully: boolean = false;
  public userNotEnabled: boolean = false;

  constructor(
    private http: HttpClient,
    public commonRedirectsService: CommonRedirectsService
  ) { }

  ngOnInit(): void {
  }

  public remindPassword(): void {
    const url = '/api/password-reminder';
    const observer: Observer<any> = {
      next: (response) => {
        console.log(response);
        this.authenticationError = false;
        this.userNotEnabled = false;
        this.authenticatedSuccessfully = true;
      },
      error: (error) => {
        if (error.status === 400 && error.error.message === 'Card number not authenticated') {
          this.authenticationError = true;
          this.authenticatedSuccessfully = false;
          this.userNotEnabled = false;
        }
        if (error.status === 403 && error.error.message === 'User not enabled') {
          this.userNotEnabled = true;
          this.authenticationError = false;
          this.authenticatedSuccessfully = false;
        }
      },
      complete: () => {
      },
    };
  
    this.http.post<any>(url, {
      username: this.model.username,
      cardNumber: this.model.cardNumber
    }).subscribe(observer);
  }

}
