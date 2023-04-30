import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { RequestInterceptor } from './request/request.interceptor';
import { RouterModule } from '@angular/router';
import { RegistrationComponent } from './registration/registration.component';
import { RegisteredComponent } from './registered/registered.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { BooksComponent } from './user-profile/books/books.component';
import { ChangePasswordComponent } from './user-profile/change-password/change-password.component';
import { NavigationMenuComponent } from './navigation-menu/navigation-menu.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent,
    HomeComponent,
    RegisteredComponent,
    UserProfileComponent,
    BooksComponent,
    ChangePasswordComponent,
    NavigationMenuComponent
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
