package com.stackroute.keepnote.controller;

import com.stackroute.keepnote.exception.ReminderNotCreatedException;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.model.Reminder;
import com.stackroute.keepnote.service.ReminderService;
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
 * @CrossOrigin,@EnableFeignClients and @RibbonClient
 *
 */
@RestController
@CrossOrigin
@RequestMapping("api/v1")
public class ReminderController {

    /*
     * From the problem statement, we can understand that the application requires
     * us to implement five functionalities regarding reminder. They are as
     * following:
     *
     * 1. Create a reminder
     * 2. Delete a reminder
     * 3. Update a reminder
     * 4. Get all reminders by userId
     * 5. Get a specific reminder by id.
     *
     */
    private ReminderService reminderService;
    private ResponseEntity responseEntity;

    /*
     * Autowiring should be implemented for the ReminderService. (Use
     * Constructor-based autowiring) Please note that we should not create any
     * object using the new keyword
     */

    @Autowired
    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }


    /*
     * Define a handler method which will create a reminder by reading the
     * Serialized reminder object from request body and save the reminder in
     * database. Please note that the reminderId has to be unique. This handler
     * method should return any one of the status messages basis on different
     * situations:
     * 1. 201(CREATED - In case of successful creation of the reminder
     * 2. 409(CONFLICT) - In case of duplicate reminder ID
     *
     * This handler method should map to the URL "/api/v1/reminder" using HTTP POST
     * method".
     */
    @PostMapping("/reminder")
    public ResponseEntity<?> createReminder(@RequestBody Reminder reminder) {
        try {
            Reminder createdReminder = reminderService.createReminder(reminder);
            responseEntity = new ResponseEntity(createdReminder, HttpStatus.CREATED);
        } catch (ReminderNotCreatedException e) {
            responseEntity = new ResponseEntity("Reminder already exists", HttpStatus.CONFLICT);
        } catch (Exception e) {
            responseEntity = new ResponseEntity("Some Internal Error Try after sometime", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    /*
     * Define a handler method which will delete a reminder from a database.
     *
     * This handler method should return any one of the status messages basis on
     * different situations:
     * 1. 200(OK) - If the reminder deleted successfully from database.
     * 2. 404(NOT FOUND) - If the reminder with specified reminderId is not found.
     *
     * This handler method should map to the URL "/api/v1/reminder/{id}" using HTTP Delete
     * method" where "id" should be replaced by a valid reminderId without {}
     */

    @DeleteMapping("/reminder/{id}")
    public ResponseEntity<?> deleteReminder(@PathVariable String id) {
        try {
            reminderService.deleteReminder(id);
            responseEntity = new ResponseEntity("Reminder deleted", HttpStatus.OK);
        } catch (ReminderNotFoundException e) {
            responseEntity = new ResponseEntity("User already exists", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            responseEntity = new ResponseEntity("Some Internal Error Try after sometime", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    /*
     * Define a handler method which will update a specific reminder by reading the
     * Serialized object from request body and save the updated reminder details in
     * a database. This handler method should return any one of the status messages
     * basis on different situations:
     * 1. 200(OK) - If the reminder updated successfully.
     * 2. 404(NOT FOUND) - If the reminder with specified reminderId is not found.
     *
     * This handler method should map to the URL "/api/v1/reminder/{id}" using HTTP PUT
     * method.
     */
    @PutMapping("/reminder/{id}")
    public ResponseEntity<?> updateReminder(@PathVariable String id, @RequestBody Reminder reminder) {
        try {
            Reminder updatedReminder = reminderService.updateReminder(reminder, id);
            responseEntity = new ResponseEntity(updatedReminder, HttpStatus.OK);
        } catch (ReminderNotFoundException e) {
            responseEntity = new ResponseEntity("Reminder does not exist", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            responseEntity = new ResponseEntity("Some Internal Error Try after sometime", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    /*
     * Define a handler method which will show details of a specific reminder. This
     * handler method should return any one of the status messages basis on
     * different situations:
     * 1. 200(OK) - If the reminder found successfully.
     * 2. 404(NOT FOUND) - If the reminder with specified reminderId is not found.
     *
     * This handler method should map to the URL "/api/v1/reminder/{id}" using HTTP GET method
     * where "id" should be replaced by a valid reminderId without {}
     */
    @GetMapping("/reminder/{id}")
    public ResponseEntity<?> getReminder(@PathVariable String id) {
        try {
            Reminder reminder = reminderService.getReminderById(id);
            responseEntity = new ResponseEntity(reminder, HttpStatus.OK);
        } catch (ReminderNotFoundException e) {
            responseEntity = new ResponseEntity("Reminder does not exist", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            responseEntity = new ResponseEntity("Some Internal Error Try after sometime", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    /*
     * Define a handler method which will get us the all reminders.
     * This handler method should return any one of the status messages basis on
     * different situations:
     * 1. 200(OK) - If the reminder found successfully.
     * 2. 404(NOT FOUND) - If the reminder with specified reminderId is not found.
     *
     * This handler method should map to the URL "/api/v1/reminder" using HTTP GET method
     */
    @GetMapping("/reminder")
    public ResponseEntity<?> getAllReminders() {
        try {
            List<Reminder> list = reminderService.getAllReminders();
            responseEntity = new ResponseEntity(list, HttpStatus.OK);
        } catch (Exception e) {
            responseEntity = new ResponseEntity("Some Internal Error Try after sometime", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
}
