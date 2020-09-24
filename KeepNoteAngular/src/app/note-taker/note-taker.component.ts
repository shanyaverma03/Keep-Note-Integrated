import { Component, OnInit } from '@angular/core';
import { Note } from '../note';
import { NotesService } from '../services/notes.service';


@Component({
  selector: 'app-note-taker',
  templateUrl: './note-taker.component.html',
  styleUrls: ['./note-taker.component.css']
})

export class NoteTakerComponent {
  public note: Note;
  public errMessage: string;

  constructor(private notesService: NotesService) {
    this.note = new Note();
  }

  addNote() {
    this.notesService.addNote(this.note).subscribe(
      data => {
        if (this.note.noteTitle === '' || this.note.noteContent === '') {
          this.errMessage = 'Title and Text both are required fields';
        }

      },
      error => {
        this.errMessage = error.message;
      }

    );
    this.note = new Note();

  }


}

