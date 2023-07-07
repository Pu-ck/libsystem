import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserEnabledService } from 'src/app/services/user/user-enabled.service';

@Component({
  selector: 'app-favourites',
  templateUrl: './favourites.component.html',
  styleUrls: ['./favourites.component.css']
})
export class FavouritesComponent implements OnInit {

  public favouriteBooks: any = [];

  constructor(
    private http: HttpClient,
    private userEnabledService: UserEnabledService
  ) { }

  ngOnInit(): void {
    this.getUserFavouriteBooks();
  }

  private getUserFavouriteBooks(): void {
    const url = 'api/userprofile/favourites';
    this.http.get<any>(url, {}).subscribe(response => {
      this.favouriteBooks = response;
    }, error => {
      this.userEnabledService.validateIfUserIsEnabled(error);
    }
    );
  }

}
