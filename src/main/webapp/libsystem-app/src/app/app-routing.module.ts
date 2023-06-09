import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthenticationGuard } from './authentication/general/authentication.guard';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisteredComponent } from './components/registered/registered.component';
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

const routes: Routes = [
  {path: '', canActivate:[AuthenticationGuard], children: [
    { path: '', component: HomeComponent },
    { path: 'login', component: LoginComponent},
    { path: 'registration', component: RegistrationComponent },
    { path: 'registered', component: RegisteredComponent },
    { path: 'user-profile', component: UserProfileComponent },
    { path: 'user-profile/books', component: UserBooksComponent },
    { path: 'user-profile/change-password', component: ChangePasswordComponent },
    { path: 'user-profile/favourites', component: FavouritesComponent },
    { path: 'books', component: BooksComponent },
    { path: 'books/:id', component: BookDetailsComponent },
    { path: 'books/:id/borrow-book', component: BorrowBookComponent },
    { path: 'books/:id/borrow-book/borrowed', component: BorrowedComponent },
    { path: 'password-reminder', component: PasswordReminderComponent },
    { path: 'password-reminder/new-password', component: NewPasswordComponent },
    { path: '', canActivate:[AdministratorGuard], children: [
      { path: 'administration', component: AdministrationComponent },
      { path: 'administration/books', component: AdminBooksComponent },
      { path: 'administration/users', component: UsersComponent },
      { path: 'administration/card-numbers', component: CardNumbersComponent }
    ]}
  ]},
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }