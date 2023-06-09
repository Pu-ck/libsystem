import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  public newBooks: any;

  constructor(
    private http: HttpClient
  ) { }

  ngOnInit(): void {
    this.getNewBooks();
  }

  private getNewBooks(): void {
    const url = '/api/home';
    this.http.get<any>(url, { }).subscribe(response => {
      this.newBooks = response;
    }, error => {
        console.log(error);
      }
    );
  }

}