import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthenticationGuard } from './authentication/authentication.guard';
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

const routes: Routes = [
  {path: '', canActivate:[AuthenticationGuard], children: [
    { path: '', component: HomeComponent },
    { path: 'login', component: LoginComponent},
    { path: 'registration', component: RegistrationComponent },
    { path: 'registered', component: RegisteredComponent },
    { path: 'user-profile', component: UserProfileComponent },
    { path: 'user-profile/books', component: UserBooksComponent },
    { path: 'user-profile/change-password', component: ChangePasswordComponent },
    { path: 'books', component: BooksComponent },
    { path: 'books/:id', component: BookDetailsComponent },
    { path: 'books/:id/borrow-book', component: BorrowBookComponent },
    { path: 'password-reminder', component: PasswordReminderComponent },
    { path: 'password-reminder/new-password', component: NewPasswordComponent },
  ]},
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }