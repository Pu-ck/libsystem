import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-favourites',
  templateUrl: './favourites.component.html',
  styleUrls: ['./favourites.component.css']
})
export class FavouritesComponent implements OnInit {

  public favouriteBooks: any;

  constructor(
    private http: HttpClient
  ) { }

  ngOnInit(): void {
    this.getUserFavouriteBooks();
  }

  private getUserFavouriteBooks(): void {
    const url = 'api/userprofile/favourites';
    this.http.get<any>(url, { }).subscribe(response => {
      this.favouriteBooks = response;
    }, error => {
        console.log(error);
      }
    );
  }

}
