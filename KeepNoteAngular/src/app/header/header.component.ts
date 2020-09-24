import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RouterService } from '../services/router.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  isNoteView = true;
  constructor(private routerService: RouterService) {

  }

  toListView() {
    this.routerService.routeToListView();
    this.isNoteView = false;
  }

  toNoteView() {
    this.routerService.routeToNoteView();
    this.isNoteView = true;
  }
}
