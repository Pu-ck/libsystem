import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-borrow-book',
  templateUrl: './borrow-book.component.html',
  styleUrls: ['./borrow-book.component.css']
})
export class BorrowBookComponent implements OnInit {

  private id: string = '';

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.validateBookId();
  }

  private validateBookId() {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.id = params.get('id')!;
    });

    const url = `api/books/${this.id}`;
    this.http.get<any>(url, { }).subscribe(response => {
      console.log(response);
    }, error => {
        if (error.status === 404 && error.error.message === 'Book not found') {
            console.log(error);
            this.router.navigate(['/']);
        }
      }
    );
  }

}
