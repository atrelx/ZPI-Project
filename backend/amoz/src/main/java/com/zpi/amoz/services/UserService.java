package com.zpi.amoz.services;

import com.zpi.amoz.models.User;
import com.zpi.amoz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserById(String id) {
        if (id != null) {
            return userRepository.findById(id);
        } else {
            return Optional.empty();
        }
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

}