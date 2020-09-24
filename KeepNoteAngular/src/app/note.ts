export class Note {
  noteId: number;
  noteTitle: string;
  noteContent: string;
  noteStatus: string;
  category : any;
  reminders : any;
  noteCreatedBy:String

  constructor() {
    this.noteId = 1;
    this.noteTitle = '';
    this.noteContent = '';
    this.noteStatus = 'not-started';
    this.category = null;
    this.reminders = null;
    this.noteCreatedBy = '';
  }
}
