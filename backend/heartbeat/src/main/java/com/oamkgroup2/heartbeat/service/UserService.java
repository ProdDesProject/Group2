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

    private final Gson GSON = new Gson();
    private static final Logger LOG = Logger.getLogger(UserService.class.getName());

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
