import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

@Component({
  selector: 'app-borrowed',
  templateUrl: './borrowed.component.html',
  styleUrls: ['./borrowed.component.css']
})
export class BorrowedComponent implements OnInit {

  public borrowedBookValidated: boolean = false;

  constructor(
    private router: Router
  ) { }

  ngOnInit(): void {
    this.checkIfUserHasBorrowedBook();
    this.borrowedBookValidated = true;
  }

  private checkIfUserHasBorrowedBook(): void {
    const hasBorrowedBook = localStorage.getItem('hasBorrowedBook');
    if (hasBorrowedBook !== 'true') {
      this.router.navigateByUrl('/');
    }
  }

  public redirectToBookCatalogue(): void {
    this.router.navigateByUrl('/books');
  }

}
