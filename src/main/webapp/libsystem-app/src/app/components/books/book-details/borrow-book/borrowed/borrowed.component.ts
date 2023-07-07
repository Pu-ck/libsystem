import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonRedirectsService } from 'src/app/services/redirects/common-redirects.service';

@Component({
  selector: 'app-borrowed',
  templateUrl: './borrowed.component.html',
  styleUrls: ['./borrowed.component.css']
})
export class BorrowedComponent implements OnInit {

  public borrowedBookValidated: boolean = false;

  constructor(
    private router: Router,
    private commonRedirectsService: CommonRedirectsService
  ) { }

  ngOnInit(): void {
    this.commonRedirectsService.checkSingleVisitPageSessionStorageCondition('hasBorrowedBook');
    this.borrowedBookValidated = true;
  }

  public redirectToBookCatalogue(): void {
    this.router.navigateByUrl('/books');
  }

}
