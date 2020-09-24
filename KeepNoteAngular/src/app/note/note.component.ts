import { Component, Input, OnInit } from '@angular/core';
import { Note } from '../note';
import { RouterService } from '../services/router.service';

@Component({
  selector: 'app-note',
  templateUrl: './note.component.html',
  styleUrls: ['./note.component.css']
})
export class NoteComponent implements OnInit {


  constructor(private routerService: RouterService) {

  }


  @Input()
  note: Note;

  ngOnInit() {
  }

  editNote() {
    const noteId = this.note.noteId;
    this.routerService.routeToEditNoteView(noteId);
  }
}
