import {Component, inject, OnInit} from '@angular/core';
import {OidcSecurityService} from "angular-auth-oidc-client";
import {RouterModule} from "@angular/router";
import {HeaderComponent} from "./shared/header/header";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule, HeaderComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  title = 'microservices-shop-frontend';

  private readonly oidcSecurityService = inject(OidcSecurityService);

  ngOnInit(): void {
    this.oidcSecurityService
      .checkAuth()
      .subscribe(({isAuthenticated}) => {
        console.log('app authenticated', isAuthenticated);
      })
  }
}
