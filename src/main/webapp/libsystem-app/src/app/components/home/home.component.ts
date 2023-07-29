import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserEnabledService } from 'src/app/services/user/user-enabled.service';
import { NewBook } from 'src/app/models/books/new-book';
import { TranslationService } from 'src/app/services/translation/translation.service';
import { Observer } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  public newBooks: NewBook[] = [];

  constructor(
    private http: HttpClient,
    private userEnabledService: UserEnabledService,
    private translation: TranslationService
  ) { }

  ngOnInit(): void {
    this.getNewBooks();
  }

  private getNewBooks(): void {
    const url = '/api/home';
    const observer: Observer<any> = {
      next: (response) => {
        this.newBooks = response;
        for (let newBook of response) {
          const genresArray = newBook.genres.split(',').map((genre: string) => genre.trim());
          const translatedGenresArray = genresArray.map((genre: string) => this.translation.translateGenre(genre));
          newBook.genres = translatedGenresArray.join(', ');
        }
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