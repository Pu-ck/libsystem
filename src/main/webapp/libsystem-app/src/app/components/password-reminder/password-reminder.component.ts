import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonRedirectsService } from 'src/app/services/redirects/common-redirects.service';

@Component({
  selector: 'app-password-reminder',
  templateUrl: './password-reminder.component.html',
  styleUrls: ['./password-reminder.component.css']
})
export class PasswordReminderComponent implements OnInit {

  public model: any = {};
  public authenticationError: boolean = false;
  public authenticatedSuccessfully: boolean = false;

  constructor(
    private http: HttpClient,
    public commonRedirectsService: CommonRedirectsService
  ) { }

  ngOnInit(): void {
  }

  public remindPassword(): void {
    const url = '/api/password-reminder';
    this.http.post<any>(url, {
      username: this.model.username,
      cardNumber: this.model.cardNumber
    }).subscribe(response => {
        console.log(response);
        this.authenticationError = false;
        this.authenticatedSuccessfully = true;
    }, error => {
      if (error.status === 400 && error.error.message === 'Card number not authenticated') {
        this.authenticationError = true;
        this.authenticatedSuccessfully = false;
      }
    }
    );
  }

}
