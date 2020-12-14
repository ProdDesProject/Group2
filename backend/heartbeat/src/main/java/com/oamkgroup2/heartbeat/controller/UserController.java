package com.oamkgroup2.heartbeat.controller;

import java.util.List;

import javax.validation.Valid;

import com.oamkgroup2.heartbeat.exception.EntityNotFoundException;
import com.oamkgroup2.heartbeat.model.User;
import com.oamkgroup2.heartbeat.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API controller for handling requests about users.
 */
@RestController
@Validated
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Get details about a specific user.
     * 
     * @param id the user to get.
     * @return JSON representation of the user object.
     * @throws EntityNotFoundException
     */
    @GetMapping("/get")
    public ResponseEntity<User> getUserById(@RequestBody long id) throws EntityNotFoundException {
        return new ResponseEntity<>(this.userService.getUserById(id), HttpStatus.OK);
    }

    /**
     * Persist a new user.
     * 
     * @param user JSON representation of the user to save.
     * @return the persisted user.
     */
    @PostMapping("/new")
    public ResponseEntity<User> createNewUser(@Valid @RequestBody User user) {
        return new ResponseEntity<>(this.userService.newUser(user), HttpStatus.OK);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(this.userService.getAll(), HttpStatus.OK);
    }

}
