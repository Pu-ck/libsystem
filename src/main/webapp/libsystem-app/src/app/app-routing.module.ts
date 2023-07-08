import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthenticationGuard } from './authentication/general/authentication.guard';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisteredComponent } from './components/registration/registered/registered.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { UserBooksComponent } from './components/user-profile/user-books/user-books.component';
import { ChangePasswordComponent } from './components/user-profile/change-password/change-password.component';
import { PasswordReminderComponent } from './components/password-reminder/password-reminder.component';
import { NewPasswordComponent } from './components/password-reminder/new-password/new-password.component';
import { BooksComponent } from './components/books/books.component';
import { BookDetailsComponent } from './components/books/book-details/book-details.component';
import { BorrowBookComponent } from './components/books/book-details/borrow-book/borrow-book.component';
import { BorrowedComponent } from './components/books/book-details/borrow-book/borrowed/borrowed.component';
import { FavouritesComponent } from './components/user-profile/favourites/favourites.component';
import { AdministrationComponent } from './components/administration/administration.component';
import { AdministratorGuard } from './authentication/administrator/administrator.guard';
import { UsersComponent } from './components/administration/users/users.component';
import { CardNumbersComponent } from './components/administration/card-numbers/card-numbers.component';
import { AdminBooksComponent } from './components/administration/admin-books/admin-books.component';
import { UserEnabledStatusComponent } from './components/administration/users/user-enabled-status/user-enabled-status.component';
import { UserEnabledStatusUpdatedComponent } from './components/administration/users/user-enabled-status/user-enabled-status-updated/user-enabled-status-updated.component';
import { RegisterCardNumberComponent } from './components/administration/card-numbers/register-card-number/register-card-number.component';
import { CardNumberRegisteredComponent } from './components/administration/card-numbers/register-card-number/card-number-registered/card-number-registered.component';
import { ExtendBookComponent } from './components/administration/admin-books/extend-book/extend-book.component';
import { ExtendedBookComponent } from './components/administration/admin-books/extend-book/extended-book/extended-book.component';

const routes: Routes = [
  {
    path: '', canActivate: [AuthenticationGuard], children: [
      { path: '', component: HomeComponent },
      { path: 'login', component: LoginComponent },
      { path: 'registration', component: RegistrationComponent },
      { path: 'registered', component: RegisteredComponent },
      { path: 'password-reminder', component: PasswordReminderComponent },
      { path: 'password-reminder/new-password/:token', component: NewPasswordComponent },
      { path: 'user-profile', component: UserProfileComponent },
      { path: 'user-profile/books', component: UserBooksComponent },
      { path: 'user-profile/change-password', component: ChangePasswordComponent },
      { path: 'user-profile/favourites', component: FavouritesComponent },
      { path: 'books', component: BooksComponent },
      { path: 'books/:id', component: BookDetailsComponent },
      { path: 'books/:id/borrow-book', component: BorrowBookComponent },
      { path: 'books/:id/borrow-book/borrowed', component: BorrowedComponent },
      {
        path: '', canActivate: [AdministratorGuard], children: [
          { path: 'administration', component: AdministrationComponent },
          { path: 'administration/books', component: AdminBooksComponent },
          { path: 'administration/users', component: UsersComponent },
          { path: 'administration/users/:id/user-enabled-status', component: UserEnabledStatusComponent },
          { path: 'administration/users/:id/user-enabled-status/user-enabled-status-updated', component: UserEnabledStatusUpdatedComponent },
          { path: 'administration/card-numbers', component: CardNumbersComponent },
          { path: 'administration/card-numbers/register-card-number', component: RegisterCardNumberComponent },
          { path: 'administration/card-numbers/register-card-number/:cardNumber/card-number-registered', component: CardNumberRegisteredComponent },
          { path: 'administration/books/:id/extend-book', component: ExtendBookComponent },
          { path: 'administration/books/:id/extend-book/extended-book', component: ExtendedBookComponent }
        ]
      }
    ]
  },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }