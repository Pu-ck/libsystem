import { Component, OnInit } from '@angular/core';
import { UserEnabledService } from 'src/app/services/user/user-enabled.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonRedirectsService } from 'src/app/services/redirects/common-redirects.service';

@Component({
  selector: 'app-register-card-number',
  templateUrl: './register-card-number.component.html',
  styleUrls: ['./register-card-number.component.css']
})
export class RegisterCardNumberComponent implements OnInit {

  public model: any = {};
  public cardNumberAlreadyRegistered: boolean = false;
  public peselNumberAlreadyRegistered: boolean = false;

  constructor(
    private userEnabledService: UserEnabledService,
    private http: HttpClient,
    private router: Router,
    public commonRedirectsService: CommonRedirectsService
  ) { }

  ngOnInit(): void {
  }

  public registerCardNumber(): void {
    const url = `/api/administration/card-numbers/register-card-number`
    this.http.post<any>(url, {
      cardNumber: this.model.cardNumber, peselNumber: this.model.peselNumber
    }).subscribe(response => {
      console.log(response);
      sessionStorage.setItem('hasRegisteredCardNumber', 'true');
      this.router.navigateByUrl(`/administration/card-numbers/register-card-number/${this.model.cardNumber}/card-number-registered`);
    }, error => {
      this.userEnabledService.validateIfUserIsEnabled(error);
      if (error.status === 409) {
        if (error.error.message === 'Card number already taken') {
          this.peselNumberAlreadyRegistered = false;
          this.cardNumberAlreadyRegistered = true;
        }
        else if (error.error.message = 'PESEL number already taken') {
          this.cardNumberAlreadyRegistered = false;
          this.peselNumberAlreadyRegistered = true;
        }
      }
    }
    );
  }

}
