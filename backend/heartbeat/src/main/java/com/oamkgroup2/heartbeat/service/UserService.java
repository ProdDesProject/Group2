package com.oamkgroup2.heartbeat.service;

import com.google.gson.Gson;
import com.oamkgroup2.heartbeat.model.User;
import com.oamkgroup2.heartbeat.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Tool for converting to and from JSON and POJO objects.
     */
    private final Gson GSON = new Gson();
    private static final Logger LOG = Logger.getLogger(UserService.class.getName());

    /**
     * Creates a new user from a JSON string representation of a user and persist
     * it.
     * 
     * @param stringUser JSON representation of User object.
     * @return the created user.
     */
    public User newUser(String stringUser) {
        // TODO: ensure no duplicate users can be created
        try {
            User newUser = GSON.fromJson(stringUser, User.class);
            return userRepository.save(newUser);
        } catch (Exception e) {
            LOG.warning(e.getLocalizedMessage());
            // TODO: fix error handling
            return null;
        }
    }

    /**
     * Find a user by its id. Returns null if no user is found.
     * 
     * @param the id to find.
     * @return the user object.
     */
    public User getUserById(long id) {
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                return user.get();
            }
            return null; // TODO: fix error handling
        } catch (Exception e) {
            LOG.warning(e.getLocalizedMessage());
            return null; // TODO: fix error handling
        }
    }

}
