import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserEnabledService } from 'src/app/services/user/user-enabled.service';
import { NewBook } from 'src/app/models/books/new-book';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  public newBooks: NewBook[] = [];

  constructor(
    private http: HttpClient,
    private userEnabledService: UserEnabledService
  ) { }

  ngOnInit(): void {
    this.getNewBooks();
  }

  private getNewBooks(): void {
    const url = '/api/home';
    this.http.get<any>(url, {}).subscribe(response => {
      this.newBooks = response;
    }, error => {
      this.userEnabledService.validateIfUserIsEnabled(error);
    }
    );
  }

}