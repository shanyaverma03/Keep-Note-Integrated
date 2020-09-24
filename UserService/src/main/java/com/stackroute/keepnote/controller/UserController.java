package com.stackroute.keepnote.controller;

import com.stackroute.keepnote.exceptions.UserAlreadyExistsException;
import com.stackroute.keepnote.exceptions.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * As in this assignment, we are working on creating RESTful web service, hence annotate
 * the class with @RestController annotation. A class annotated with the @Controller annotation
 * has handler methods which return a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 * 
 * @CrossOrigin,@EnableFeignClients,@RibbonClient needs to be added.
 */
@RestController
@RequestMapping("api/v1")
@CrossOrigin
public class UserController {

	/*
	 * Autowiring should be implemented for the UserService. (Use Constructor-based
	 * autowiring) Please note that we should not create an object using the new
	 * keyword
	 */
	private UserService userService;
	private ResponseEntity responseEntity;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	/*
	 * Define a handler method which will create a specific user by reading the
	 * Serialized object from request body and save the user details in the
	 * database. This handler method should return any one of the status messages
	 * basis on different situations:
	 * 1. 201(CREATED) - If the user created successfully. 
	 * 2. 409(CONFLICT) - If the userId conflicts with any existing user
	 * 
	 * This handler method should map to the URL "/user" using HTTP POST method
	 */
	@PostMapping("/user")
	public ResponseEntity<?> createUser(@RequestBody User user) {
		try {
			User registeredUser = userService.registerUser(user);
			responseEntity = new ResponseEntity(user, HttpStatus.CREATED);
		} catch (UserAlreadyExistsException e) {
			responseEntity = new ResponseEntity("User already exists", HttpStatus.CONFLICT);
		} catch (Exception e) {
			responseEntity = new ResponseEntity("Some Internal Error Try after sometime", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}

	/*
	 * Define a handler method which will update a specific user by reading the
	 * Serialized object from request body and save the updated user details in a
	 * database. This handler method should return any one of the status messages
	 * basis on different situations: 
	 * 1. 200(OK) - If the user updated successfully.
	 * 2. 404(NOT FOUND) - If the user with specified userId is not found.
	 * 
	 * This handler method should map to the URL "/api/v1/user/{id}" using HTTP PUT method.
	 */
	@PutMapping("/user/{id}")
	public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User user) {
		try {
			User registeredUser = userService.updateUser(id, user);
			responseEntity = new ResponseEntity(user, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			responseEntity = new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			responseEntity = new ResponseEntity("Some Internal Error Try after sometime", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}

	/*
	 * Define a handler method which will delete a user from a database.
	 * This handler method should return any one of the status messages basis on
	 * different situations: 
	 * 1. 200(OK) - If the user deleted successfully from database. 
	 * 2. 404(NOT FOUND) - If the user with specified userId is not found.
	 *
	 * This handler method should map to the URL "/api/v1/user/{id}" using HTTP Delete
	 * method" where "id" should be replaced by a valid userId without {}
	 */
	@DeleteMapping("/user/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable String id) {
		try {
			boolean status = userService.deleteUser(id);
			responseEntity = new ResponseEntity("User deleted", HttpStatus.OK);
		} catch (UserNotFoundException e) {
			responseEntity = new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			responseEntity = new ResponseEntity("Some Internal Error Try after sometime", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}

	/*
	 * Define a handler method which will show details of a specific user. This
	 * handler method should return any one of the status messages basis on
	 * different situations: 
	 * 1. 200(OK) - If the user found successfully. 
	 * 2. 404(NOT FOUND) - If the user with specified userId is not found. 
	 * This handler method should map to the URL "/api/v1/user/{id}" using HTTP GET method where "id" should be
	 * replaced by a valid userId without {}
	 */
	@GetMapping("/user/{id}")
	public ResponseEntity<?> getUser(@PathVariable String id) {
		try {
			User user = userService.getUserById(id);
			responseEntity = new ResponseEntity(user, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			responseEntity = new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			responseEntity = new ResponseEntity("Some Internal Error Try after sometime", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
}
