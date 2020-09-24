package com.stackroute.keepnote.controller;


import com.stackroute.keepnote.exception.UserAlreadyExistsException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserAuthenticationService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * As in this assignment, we are working on creating RESTful web service, hence annotate
 * the class with @RestController annotation. A class annotated with the @Controller annotation
 * has handler methods which return a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized
 * format. Starting from Spring 4 and above, we can use @RestController annotation which
 * is equivalent to using @Controller and @ResposeBody annotation
 */

@RestController
@RequestMapping("api/v1/auth")
@RibbonClient(name = "auth")
public class UserAuthenticationController {

    /*
     * Autowiring should be implemented for the UserAuthenticationService. (Use Constructor-based
     * autowiring) Please note that we should not create an object using the new
     * keyword
     */
    private UserAuthenticationService userAuthenticationService;
    private ResponseEntity responseEntity;
    private Map<String, String> map = new HashMap<>();

    @Autowired
    public UserAuthenticationController(UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    /*
     * Define a handler method which will create a specific user by reading the
     * Serialized object from request body and save the user details in the
     * database. This handler method should return any one of the status messages
     * basis on different situations:
     * 1. 201(CREATED) - If the user created successfully.
     * 2. 409(CONFLICT) - If the userId conflicts with any existing user
     *
     * This handler method should map to the URL "/api/v1/auth/register" using HTTP POST method
     */

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody User user) {
        try {
            userAuthenticationService.saveUser(user);
            responseEntity = new ResponseEntity("User registered successfully", HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            responseEntity = new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            responseEntity = new ResponseEntity("Some Internal Error Try after sometime", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }


    /* Define a handler method which will authenticate a user by reading the Serialized user
     * object from request body containing the username and password. The username and password should be validated
     * before proceeding ahead with JWT token generation. The user credentials will be validated against the database entries.
     * The error should be return if validation is not successful. If credentials are validated successfully, then JWT
     * token will be generated. The token should be returned back to the caller along with the API response.
     * This handler method should return any one of the status messages basis on different
     * situations:
     * 1. 200(OK) - If login is successful
     * 2. 401(UNAUTHORIZED) - If login is not successful
     *
     * This handler method should map to the URL "/api/v1/auth/login" using HTTP POST method
     */
    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody User user) {
        try {
            String jwtToken = getToken(user.getUserId(), user.getUserPassword());
            map.put("message", "User successfully logged in");
            map.put("token", jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", e.getMessage());
            map.put("token", null);
            return new ResponseEntity(map, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity(map, HttpStatus.OK);
    }


    // Generate JWT token
    public String getToken(String username, String password) throws Exception {
        String jwtToken = "";
        if (username == null || password == null) {
            throw new Exception("Please send valid username or password");
        }
        try {
            User user = userAuthenticationService.findByUserIdAndPassword(username, password);
            jwtToken = Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .signWith(SignatureAlgorithm.HS256, "secretkey")
                    .compact();
        } catch (UserNotFoundException e) {
            throw new Exception("Invalid credentials");
        }
        return jwtToken;
    }


}
