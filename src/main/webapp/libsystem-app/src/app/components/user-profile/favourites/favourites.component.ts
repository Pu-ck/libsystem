import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserEnabledService } from 'src/app/services/user/user-enabled.service';

@Component({
  selector: 'app-favourites',
  templateUrl: './favourites.component.html',
  styleUrls: ['./favourites.component.css']
})
export class FavouritesComponent implements OnInit {

  public bookId!: number;
  public currentPage: number = 1;
  public itemsPerPage: number = 6;
  public favouriteBooks: any = [];
  public favouriteBooksListEmpty!: boolean;

  constructor(
    private http: HttpClient,
    private userEnabledService: UserEnabledService
  ) { }

  ngOnInit(): void {
    this.getUserFavouriteBooks();
  }

  public getFirstItemIndex(): number {
    return (this.currentPage - 1) * this.itemsPerPage;
  }

  public getLastItemIndex(): number {
    const lastIndex = this.currentPage * this.itemsPerPage - 1;
    return Math.min(lastIndex, this.favouriteBooks.length - 1);
  }

  public onPageChange(page: number): void {
    this.currentPage = page;
  }

  public getTotalPages(): number {
    return Math.ceil(this.favouriteBooks.length / this.itemsPerPage);
  }

  public removeFromFavourites(event: Event, bookId: number): void {
    event.stopPropagation();
    const url = `/api/books/${bookId}/remove-from-favourites`;
    this.http.delete<any>(url, {}).subscribe(response => {
      console.log(response);
      window.location.reload();
    }, error => {
      this.userEnabledService.validateIfUserIsEnabled(error);
    }
    );
  }

  private getUserFavouriteBooks(): void {
    const url = 'api/userprofile/favourites';
    this.http.get<any>(url, {}).subscribe(response => {
      this.favouriteBooks = response;
      if (this.favouriteBooks.length === 0) {
        this.favouriteBooksListEmpty = true;
      } else {
        this.favouriteBooksListEmpty = false;
      }
    }, error => {
      this.userEnabledService.validateIfUserIsEnabled(error);
    }
    );
  }

}
