package com.oamkgroup2.heartbeat.controller;

import com.oamkgroup2.heartbeat.model.User;
import com.oamkgroup2.heartbeat.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API controller for handling requests about users.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Get details about a specific user.
     * 
     * @param id the user to get.
     * @return JSON representation of the user object.
     */
    @GetMapping("/get")
    public User getUserById(@RequestBody long id) {
        return this.userService.getUserById(id);
    }

    /**
     * Persist a new user.
     * 
     * @param user JSON representation of the user to save.
     * @return the persisted user.
     */
    @PostMapping("/new")
    public User createNewUser(@RequestBody String user) {
        return this.userService.newUser(user);
    }

}
