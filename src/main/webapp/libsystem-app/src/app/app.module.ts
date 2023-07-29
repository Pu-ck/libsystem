import { HttpClientModule, HttpClient, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RequestInterceptor } from './request/request.interceptor';
import { RouterModule } from '@angular/router';
import { RegistrationComponent } from './components/registration/registration.component';
import { RegisteredComponent } from './components/registration/registered/registered.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { UserBooksComponent } from './components/user-profile/user-books/user-books.component';
import { ChangePasswordComponent } from './components/user-profile/change-password/change-password.component';
import { NavigationMenuComponent } from './components/navigation-menu/navigation-menu.component';
import { PasswordReminderComponent } from './components/password-reminder/password-reminder.component';
import { NewPasswordComponent } from './components/password-reminder/new-password/new-password.component';
import { BooksComponent } from './components/books/books.component';
import { BookDetailsComponent } from './components/books/book-details/book-details.component';
import { BorrowBookComponent } from './components/books/book-details/borrow-book/borrow-book.component';
import { BorrowedComponent } from './components/books/book-details/borrow-book/borrowed/borrowed.component';
import { FavouritesComponent } from './components/user-profile/favourites/favourites.component';
import { UsersComponent } from './components/administration/users/users.component';
import { CardNumbersComponent } from './components/administration/card-numbers/card-numbers.component';
import { AdminBooksComponent } from './components/administration/admin-books/admin-books.component';
import { UserEnabledStatusComponent } from './components/administration/users/user-enabled-status/user-enabled-status.component';
import { UserEnabledStatusUpdatedComponent } from './components/administration/users/user-enabled-status/user-enabled-status-updated/user-enabled-status-updated.component';
import { RegisterCardNumberComponent } from './components/administration/card-numbers/register-card-number/register-card-number.component';
import { CardNumberRegisteredComponent } from './components/administration/card-numbers/register-card-number/card-number-registered/card-number-registered.component';
import { NumericOnlyDirective } from './directives/numeric-only/numeric-only.directive';
import { ExtendBookComponent } from './components/administration/admin-books/extend-book/extend-book.component';
import { ExtendedBookComponent } from './components/administration/admin-books/extend-book/extended-book/extended-book.component';

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent,
    HomeComponent,
    RegisteredComponent,
    UserProfileComponent,
    UserBooksComponent,
    ChangePasswordComponent,
    NavigationMenuComponent,
    PasswordReminderComponent,
    NewPasswordComponent,
    BooksComponent,
    BookDetailsComponent,
    BorrowBookComponent,
    BorrowedComponent,
    FavouritesComponent,
    UsersComponent,
    CardNumbersComponent,
    AdminBooksComponent,
    UserEnabledStatusComponent,
    UserEnabledStatusUpdatedComponent,
    RegisterCardNumberComponent,
    CardNumberRegisteredComponent,
    NumericOnlyDirective,
    ExtendBookComponent,
    ExtendedBookComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    RouterModule,
    FormsModule,
    TranslateModule.forRoot({
      defaultLanguage: localStorage.getItem('language') || 'en',
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    })
  ],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: RequestInterceptor, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }
