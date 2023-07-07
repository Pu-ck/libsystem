import { Component, OnInit } from '@angular/core';
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
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.commonRedirectsService.checkSingleVisitPageSessionStorageCondition('hasRegisteredCardNumber');
    this.cardNumberRegistered = true;
    this.setCardNumber();
  }

  private setCardNumber(): void {
    this.route.paramMap.subscribe(params => {
      this.cardNumber = params.get('cardNumber') || '';
    });
  }

}
