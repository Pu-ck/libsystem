import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { CommonRedirectsService } from 'src/app/services/redirects/common-redirects.service';

@Component({
  selector: 'app-card-number-registered',
  templateUrl: './card-number-registered.component.html',
  styleUrls: ['./card-number-registered.component.css']
})
export class CardNumberRegisteredComponent implements OnInit {

  public cardNumberRegistered: boolean = false;
  public cardNumber: string = '';

  constructor(
    public commonRedirectsService: CommonRedirectsService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.checkIfCardNumberHasBeenRegistered();
    this.cardNumberRegistered = true;
    this.setCardNumber();
  }

  private checkIfCardNumberHasBeenRegistered(): void {
    const hasRegisteredCardNumber = sessionStorage.getItem('hasRegisteredCardNumber');
    if (hasRegisteredCardNumber !== 'true') {
      this.router.navigateByUrl('/');
    }
    sessionStorage.setItem('hasRegisteredCardNumber', 'false');
  } 

  private setCardNumber(): void {
    this.route.paramMap.subscribe(params => {
      this.cardNumber = params.get('cardNumber') || '';
    });
  }

}
