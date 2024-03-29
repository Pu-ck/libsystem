import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { UserEnabledService } from 'src/app/services/user/user-enabled.service';
import { CommonRedirectsService } from 'src/app/services/redirects/common-redirects.service';
import { Observer } from 'rxjs';

@Component({
  selector: 'app-extend-book',
  templateUrl: './extend-book.component.html',
  styleUrls: ['./extend-book.component.css']
})
export class ExtendBookComponent implements OnInit {

  public borrowedBookId: string = '';
  public model: any = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    private userEnabledService: UserEnabledService,
    public commonRedirectsService: CommonRedirectsService
  ) { }

  ngOnInit(): void {
    this.setBorrowedBookId();
    this.validateBorrowedBook();
  }

  public extendBorrowedBook(): void {
    const url = `/api/administration/books/${this.borrowedBookId}/extend-book`;
    const observer: Observer<any> = {
      next: (response) => {
        console.log(response);
        localStorage.setItem('hasExtendedBorrowedBook', 'true');
        this.router.navigate([`administration/books/${this.borrowedBookId}/extend-book/extended-book`]);
      },
      error: (error) => {
        console.log(error);
      },
      complete: () => {
      },
    };
  
    this.http.put<any>(url, {
      extendTime: this.model.days
    }).subscribe(observer);
  }

  private setBorrowedBookId(): void {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.borrowedBookId = String(params.get('id')!);
    });
  }

  private validateBorrowedBook(): void {
    const url = '/api/administration/books';
    const params = { borrowedBookId: this.borrowedBookId };
    const observer: Observer<any[]> = {
      next: (response) => {
        let borrowedBook = response[0];
        if (borrowedBook.accepted === false || borrowedBook.closed === true || borrowedBook.ready === false) {
          this.router.navigateByUrl('/');
        }
      },
      error: (error) => {
        this.userEnabledService.validateIfUserIsEnabled(error);
        if (error.status === 404 && error.error.message === 'Borrowed book not found' || error.status === 400) {
          this.router.navigateByUrl('/');
        }
      },
      complete: () => {
      },
    };
  
    this.http.get<any[]>(url, { params }).subscribe(observer);
  }

}
