import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  public userDetails: any;

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.getUserDetails();
  }

  private getUserDetails(): void {
    const url = '/api/userprofile';
    this.http.get<any>(url, {}).subscribe(
      response => {
        this.userDetails = response;
      },
      error => {
        console.log(error);
      }
    );
  }

}
