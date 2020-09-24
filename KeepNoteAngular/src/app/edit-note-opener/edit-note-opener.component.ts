import { Component } from '@angular/core';
import { MatDialog } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { RouterService } from '../services/router.service';
import { EditNoteViewComponent } from '../edit-note-view/edit-note-view.component';

@Component({
  selector: 'app-edit-note-opener',
  templateUrl: './edit-note-opener.component.html',
  styleUrls: ['./edit-note-opener.component.css']
})
export class EditNoteOpenerComponent {

  constructor(private dialog: MatDialog, private activatedRoute: ActivatedRoute, private route: RouterService) {
    this.activatedRoute.params.subscribe((params) => {
      const id = params.noteId;
      this.dialog.open(EditNoteViewComponent, {
        data: {
          noteId: id
        }
      }).afterClosed().subscribe(result => {
        this.route.routeBack();
      });
    });
  }
}
