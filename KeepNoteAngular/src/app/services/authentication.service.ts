import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';


@Injectable(
  {
    providedIn: 'root'
  }
)

export class AuthenticationService {

  constructor(public httpClient: HttpClient) {


  }

  authenticateUser(data) {

    const authenticated = this.httpClient.post('http://localhost:8765/user-auth/api/v1/auth/login', data);

    return authenticated;

  }

  setBearerToken(token) {
    localStorage.setItem('bearerToken', token);
  }

  getBearerToken() {
    return localStorage.getItem('bearerToken');
  }

  setUserId(userId) {
    localStorage.setItem("userId", userId);
  }

  getUserId() {
    return localStorage.getItem("userId");
  }

  isUserAuthenticated(): boolean {
    if(localStorage.getItem('bearerToken')!= undefined){
      return true;
    } else {
      return false;
    }

  }

}
