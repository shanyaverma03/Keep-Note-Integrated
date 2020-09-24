package com.stackroute.keepnote.controller;

import com.stackroute.keepnote.exception.NoteNotFoundExeption;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * As in this assignment, we are working with creating RESTful web service, hence annotate
 * the class with @RestController annotation.A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 * 
 * @CrossOrigin, @EnableFeignClients and @RibbonClient needs to be added 
 */

//@EnableFeignClients
//@RibbonClient( name = "note")
@CrossOrigin
@RestController
@RequestMapping("api/v1")
public class NoteController {

	/*
	 * Autowiring should be implemented for the NoteService. (Use Constructor-based
	 * autowiring) Please note that we should not create any object using the new
	 * keyword
	 */
	private NoteService noteService;
	private ResponseEntity responseEntity;

	@Autowired
	public NoteController(NoteService noteService) {
		this.noteService = noteService;
	}

	/*
	 * Define a handler method which will create a specific note by reading the
	 * Serialized object from request body and save the note details in the
	 * database.This handler method should return any one of the status messages
	 * basis on different situations: 1. 201(CREATED) - If the note created
	 * successfully. 2. 409(CONFLICT) - If the noteId conflicts with any existing
	 * user.
	 * 
	 * This handler method should map to the URL "/api/v1/note" using HTTP POST
	 * method
	 */
	@PostMapping("/note")
	public ResponseEntity<?> createNote(@RequestBody Note note) {
		try {
			boolean status = noteService.createNote(note);
			if (status) {
				responseEntity = new ResponseEntity(note, HttpStatus.CREATED);
			} else {
				responseEntity = new ResponseEntity("Note already exists", HttpStatus.CONFLICT);
			}
		} catch (Exception e) {
			responseEntity = new ResponseEntity("Some Internal Error Try after sometime", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}

	/*
	 * Define a handler method which will delete a note from a database. This
	 * handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the note deleted successfully from
	 * database. 2. 404(NOT FOUND) - If the note with specified noteId is not found.
	 *
	 * This handler method should map to the URL "/api/v1/note/{id}" using HTTP
	 * Delete method" where "id" should be replaced by a valid noteId without {}
	 */
	@DeleteMapping("/note/{userId}/{noteId}")
	public ResponseEntity<?> deleteNote(@PathVariable String userId, @PathVariable int noteId) {
		try {
			boolean status = noteService.deleteNote(userId, noteId);
			if (status) {
				responseEntity = new ResponseEntity("Note deleted", HttpStatus.OK);
			} else {
				responseEntity = new ResponseEntity("Note does not exist", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			responseEntity = new ResponseEntity("Some Internal Error Try after sometime", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}

	@DeleteMapping("/note/{userId}")
	public ResponseEntity<?> deleteNote(@PathVariable String userId) {
		try {
			boolean status = noteService.deleteAllNotes(userId);
			if (status) {
				responseEntity = new ResponseEntity("All Notes deleted", HttpStatus.OK);
			} else {
				responseEntity = new ResponseEntity("User does not exist", HttpStatus.NOT_FOUND);
			}
		} catch (NoteNotFoundExeption e){

			responseEntity = new ResponseEntity("Notes not found", HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			responseEntity = new ResponseEntity("", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}

	/*
	 * Define a handler method which will update a specific note by reading the
	 * Serialized object from request body and save the updated note details in a
	 * database. This handler method should return any one of the status messages
	 * basis on different situations: 1. 200(OK) - If the note updated successfully.
	 * 2. 404(NOT FOUND) - If the note with specified noteId is not found.
	 * 
	 * This handler method should map to the URL "/api/v1/note/{id}" using HTTP PUT
	 * method.
	 */

	@PutMapping("/note/{userId}/{noteId}")
	public ResponseEntity<?> updateNote(@PathVariable String userId, @PathVariable int noteId, @RequestBody Note note) {
		try {
			Note updatedNote = noteService.updateNote(note, noteId, userId);
			responseEntity = new ResponseEntity(updatedNote, HttpStatus.OK);

		} catch (NoteNotFoundExeption e) {
			responseEntity = new ResponseEntity("Note does not exist", HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			responseEntity = new ResponseEntity("Some Internal Error Try after sometime", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}

	/*
	 * Define a handler method which will get us the all notes by a userId. This
	 * handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the note found successfully.
	 * 
	 * This handler method should map to the URL "/api/v1/note" using HTTP GET
	 * method
	 */

	@GetMapping("/note/{userId}")
	public ResponseEntity<?> getAllNotes(@PathVariable String userId) {
		try {
			List<Note> list = noteService.getAllNoteByUserId(userId);
			responseEntity = new ResponseEntity(list, HttpStatus.OK);

		} catch (Exception e) {
			responseEntity = new ResponseEntity("Some Internal Error Try after sometime", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}

	/*
	 * Define a handler method which will show details of a specific note created by
	 * specific user. This handler method should return any one of the status
	 * messages basis on different situations: 1. 200(OK) - If the note found
	 * successfully. 2. 404(NOT FOUND) - If the note with specified noteId is not
	 * found. This handler method should map to the URL
	 * "/api/v1/note/{userId}/{noteId}" using HTTP GET method where "id" should be
	 * replaced by a valid reminderId without {}
	 * 
	 */

	@GetMapping("/note/{userId}/{noteId}")
	public ResponseEntity<?> getNoteById(@PathVariable String userId, @PathVariable int noteId) {
		try {
			Note note = noteService.getNoteByNoteId(userId, noteId);
			responseEntity = new ResponseEntity(note, HttpStatus.OK);

		} catch (NoteNotFoundExeption e) {
			responseEntity = new ResponseEntity("Note does not exist", HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			responseEntity = new ResponseEntity("Some Internal Error Try after sometime", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}

}
