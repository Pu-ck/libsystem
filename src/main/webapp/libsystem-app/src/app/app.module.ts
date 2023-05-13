import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RequestInterceptor } from './request/request.interceptor';
import { RouterModule } from '@angular/router';
import { RegistrationComponent } from './components/registration/registration.component';
import { RegisteredComponent } from './components/registered/registered.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { UserBooksComponent } from './components/user-profile/user-books/user-books.component';
import { ChangePasswordComponent } from './components/user-profile/change-password/change-password.component';
import { NavigationMenuComponent } from './components/navigation-menu/navigation-menu.component';
import { PasswordReminderComponent } from './components/password-reminder/password-reminder.component';
import { NewPasswordComponent } from './components/password-reminder/new-password/new-password.component';
import { BooksComponent } from './components/books/books.component';
import { BookDetailsComponent } from './components/books/book-details/book-details.component';
import { BorrowBookComponent } from './components/books/book-details/borrow-book/borrow-book.component';

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
    BorrowBookComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    RouterModule, 
    FormsModule
  ],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: RequestInterceptor, multi: true}],
  bootstrap: [AppComponent]
})
export class AppModule { }
