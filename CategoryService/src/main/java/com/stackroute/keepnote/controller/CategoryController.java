package com.stackroute.keepnote.controller;

import com.stackroute.keepnote.exception.CategoryDoesNoteExistsException;
import com.stackroute.keepnote.exception.CategoryNotCreatedException;
import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * As in this assignment, we are working with creating RESTful web service, hence annotate
 * the class with @RestController annotation.A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 * 
 * @CrossOrigin,@EnableFeignClients and @RibbonClient needs to be added 
 * 
 */
@RestController
@CrossOrigin
@RequestMapping("api/v1")
public class CategoryController {

	/*
	 * Autowiring should be implemented for the CategoryService. (Use
	 * Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword
	 */
	private CategoryService categoryService;
	private ResponseEntity responseEntity;

	@Autowired
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	/*
	 * Define a handler method which will create a category by reading the
	 * Serialized category object from request body and save the category in
	 * database. Please note that the careatorId has to be unique.This
	 * handler method should return any one of the status messages basis on
	 * different situations: 
	 * 1. 201(CREATED - In case of successful creation of the category
	 * 2. 409(CONFLICT) - In case of duplicate categoryId
	 *
	 * 
	 * This handler method should map to the URL "/api/v1/category" using HTTP POST
	 * method".
	 */
	@PostMapping("/category")
	public ResponseEntity<?> createCategory(@RequestBody Category category) {
		try {
			Category createdCategory = categoryService.createCategory(category);
			responseEntity = new ResponseEntity(createdCategory, HttpStatus.CREATED);
		} catch (CategoryNotCreatedException e) {
			responseEntity = new ResponseEntity("Category already exists", HttpStatus.CONFLICT);
		} catch (Exception e) {
			responseEntity = new ResponseEntity("Some Internal Error Try after sometime", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
	/*
	 * Define a handler method which will delete a category from a database.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the category deleted successfully from
	 * database. 2. 404(NOT FOUND) - If the category with specified categoryId is
	 * not found. 
	 * 
	 * This handler method should map to the URL "/api/v1/category/{id}" using HTTP Delete
	 * method" where "id" should be replaced by a valid categoryId without {}
	 */
	@DeleteMapping("/category/{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable String id) {
		try {
			categoryService.deleteCategory(id);
			responseEntity = new ResponseEntity("Category deleted", HttpStatus.OK);
		} catch (CategoryDoesNoteExistsException e) {
			responseEntity = new ResponseEntity("User does not exist", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			responseEntity = new ResponseEntity("Some Internal Error Try after sometime", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
	
	/*
	 * Define a handler method which will update a specific category by reading the
	 * Serialized object from request body and save the updated category details in
	 * database. This handler method should return any one of the status
	 * messages basis on different situations: 1. 200(OK) - If the category updated
	 * successfully. 2. 404(NOT FOUND) - If the category with specified categoryId
	 * is not found. 
	 * This handler method should map to the URL "/api/v1/category/{id}" using HTTP PUT
	 * method.
	 */
	@PutMapping("/category/{id}")
	public ResponseEntity<?> updateCategory(@PathVariable String id, @RequestBody Category category) {
		try {
			Category updatedCategory=categoryService.updateCategory(category, id);
			if(updatedCategory==null){
				responseEntity = new ResponseEntity("Conflict", HttpStatus.CONFLICT);
			} else {
				responseEntity = new ResponseEntity("Category updated", HttpStatus.OK);
			}
		} catch (Exception e) {
			responseEntity = new ResponseEntity("Some Internal Error Try after sometime", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
	
	/*
	 * Define a handler method which will get us the category by a userId.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the category found successfully. 
	 * 
	 * 
	 * This handler method should map to the URL "/api/v1/category" using HTTP GET method
	 */
	@GetMapping("/category/{id}")
	public ResponseEntity<?> getCategoryById(@PathVariable String id) {
		try {
			Category category = categoryService.getCategoryById(id);
			responseEntity = new ResponseEntity(category, HttpStatus.OK);
		}  catch(CategoryNotFoundException e) {
			responseEntity = new ResponseEntity("Category not found", HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			responseEntity = new ResponseEntity("Some Internal Error Try after sometime", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}



}
