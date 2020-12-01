package com.oamkgroup2.heartbeat.service;

import com.oamkgroup2.heartbeat.exception.EntityNotFoundException;

import com.oamkgroup2.heartbeat.model.User;
import com.oamkgroup2.heartbeat.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.validation.Valid;

@Service
@Validated
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger LOG = Logger.getLogger(UserService.class.getName());

    /**
     * Creates a new user from a JSON string representation of a user and persist
     * it.
     * 
     * @param stringUser JSON representation of User object.
     * @return the created user.
     */
    public User newUser(@Valid User user) {
        return userRepository.save(user);
    }

    /**
     * Find a user by its id. Returns null if no user is found.
     * 
     * @param the id to find.
     * @return the user object.
     * @throws EntityNotFoundException
     */
    public User getUserById(long id) throws EntityNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        throw new EntityNotFoundException("user with id: " + id);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

}
