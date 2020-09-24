import { Injectable } from '@angular/core';
import { Note } from '../note';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthenticationService } from './authentication.service';
import { tap } from 'rxjs/operators';

@Injectable(
  {
    providedIn: 'root'
  }
)
export class NotesService {
  public url = 'http://localhost:8765/note-service/api/v1/note';
  notes: Array<Note> = [];
  notesSubject: BehaviorSubject<Array<Note>>;

  constructor(private httpClient: HttpClient, private authService: AuthenticationService) {
    this.notesSubject = new BehaviorSubject(this.notes);
  }

  fetchNotesFromServer() {
    let userId = this.authService.getUserId();
    return this.httpClient.get<Note[]>(`http://localhost:8765/note-service/api/v1/note/${userId}`, {
      headers: new HttpHeaders().set('Authorization', `Bearer ${this.authService.getBearerToken()}`)
    }).subscribe((data) => {
      this.notes = data;
      if(data==null){
      this.notes=[];
      }
      this.notesSubject.next(this.notes);
    },
    err=>{
      console.log(err);
    });
  }

  getNotes(): BehaviorSubject<Array<Note>> {
    return this.notesSubject;
  }

  addNote(note: Note): Observable<Note> {
    let userId = this.authService.getUserId();
    note.noteCreatedBy = userId;
    return this.httpClient.post<Note>(this.url, note, {
      headers: new HttpHeaders().set('Authorization', `Bearer ${this.authService.getBearerToken()}`)
    }).pipe(tap(newNote => {
      if(this.notes==null){
      this.notes=[];
      }
      this.notes.push(newNote);
      this.notesSubject.next(this.notes);
    }));

  }

  editNote(note: Note): Observable<Note> {
    let userId = this.authService.getUserId();
    return this.httpClient.put<Note>(`http://localhost:8765/note-service/api/v1/note/${userId}/${note.noteId}`, note, {
      headers: new HttpHeaders().set('Authorization', `Bearer ${this.authService.getBearerToken()}`)
    }).pipe(tap(editedNote => {
      const note = this.notes.find(note => note.noteId == editedNote.noteId);
      Object.assign(note, editedNote);
      this.notesSubject.next(this.notes);

    }));

  }

  getNoteById(noteId): Note {
    const note = this.notes.find(note => note.noteId == noteId);
    return Object.assign({}, note);
  }
}
