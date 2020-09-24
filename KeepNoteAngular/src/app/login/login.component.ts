import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { AuthenticationService } from '../services/authentication.service';
import { Router } from '@angular/router';
import { RouterService } from '../services/router.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  public loginForm: FormGroup;
  public userId = new FormControl();
  public userPassword = new FormControl();
  public submitMessage: string;

  constructor(private authService: AuthenticationService, private routerService: RouterService) {

  }

  ngOnInit() {
    this.loginForm = new FormGroup({
      userId: new FormControl(),
      userPassword: new FormControl()
    });
  }
  loginSubmit() {
    let token;
    this.authService.authenticateUser(this.loginForm.value).subscribe(
      data => {
        console.log(data);
        token = data['token'];
        if (token != undefined) {
          console.log(token)
          this.authService.setBearerToken(token);
          this.authService.setUserId(this.loginForm.value['userId']);
          this.routerService.routeToDashboard();
        } else {
          this.routerService.routeToLogin();
        }
      },
      err => {
        if (err.status === 404) {
          this.submitMessage = err.message;
        } else {
          this.submitMessage = 'Unauthorized';
        }
      }
    );

  }
}
