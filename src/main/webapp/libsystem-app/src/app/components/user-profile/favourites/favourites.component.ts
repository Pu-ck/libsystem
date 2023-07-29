import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserEnabledService } from 'src/app/services/user/user-enabled.service';
import { PaginationService } from 'src/app/services/pagination/pagination.service';
import { FavouriteBook } from 'src/app/models/books/favourite-book';
import { CommonConstants } from 'src/app/utils/common-constants';
import { Observer } from 'rxjs';

@Component({
  selector: 'app-favourites',
  templateUrl: './favourites.component.html',
  styleUrls: ['./favourites.component.css']
})
export class FavouritesComponent implements OnInit {

  public bookId!: number;
  public currentPage: number = CommonConstants.DEFAULT_PAGE_NUMBER;
  public itemsPerPage: number = 6;
  public favouriteBooks: FavouriteBook[] = [];
  public favouriteBooksListEmpty!: boolean;

  constructor(
    private http: HttpClient,
    private userEnabledService: UserEnabledService,
    public pagination: PaginationService
  ) { }

  ngOnInit(): void {
    this.getUserFavouriteBooks();
  }

  public onPageChange(page: number): void {
    this.currentPage = page;
  }

  public removeFromFavourites(event: Event, bookId: number): void {
    event.stopPropagation();
    const url = `/api/books/${bookId}/remove-from-favourites`;
    const observer: Observer<any> = {
      next: (response) => {
        console.log(response);
        window.location.reload();
      },
      error: (error) => {
        this.userEnabledService.validateIfUserIsEnabled(error);
      },
      complete: () => {
      },
    };
  
    this.http.delete<any>(url, {}).subscribe(observer);
  }

  private getUserFavouriteBooks(): void {
    const url = 'api/userprofile/favourites';
    const observer: Observer<any> = {
      next: (response) => {
        this.favouriteBooks = response;
        this.favouriteBooksListEmpty = this.favouriteBooks.length === 0;
      },
      error: (error) => {
        this.userEnabledService.validateIfUserIsEnabled(error);
      },
      complete: () => {
      },
    };
  
    this.http.get<any>(url, {}).subscribe(observer);
  }

}
