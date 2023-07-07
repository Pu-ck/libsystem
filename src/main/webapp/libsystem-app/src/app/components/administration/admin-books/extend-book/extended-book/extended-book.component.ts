import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, ParamMap } from '@angular/router';
import { CommonRedirectsService } from 'src/app/services/redirects/common-redirects.service';

@Component({
  selector: 'app-extended-book',
  templateUrl: './extended-book.component.html',
  styleUrls: ['./extended-book.component.css']
})
export class ExtendedBookComponent implements OnInit {

  public borrowedBookId: string = '';
  public borrowedBookExtended: boolean = false;

  constructor(
    private route: ActivatedRoute,
    public commonRedirectsService: CommonRedirectsService
  ) { }

  ngOnInit(): void {
    this.commonRedirectsService.checkSingleVisitPageSessionStorageCondition('hasExtendedBorrowedBook');
    this.borrowedBookExtended = true;
    this.setBorrowedBookId();
  }

  private setBorrowedBookId(): void {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.borrowedBookId = String(params.get('id')!);
    });
  }

}
