package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUser() {
        List<User> userList = userRepository.findAll();
        return userList;
    }

    public boolean saveUser(User newUser) {
        userRepository.save(newUser);
        return true;
    }

    public List<User> getUserByEmail(String email) {
        try {
            List<User> queryResponse = userRepository.findByEmail(email);
            return queryResponse;
        } catch (Exception e) {
            throw e;
        }
    }

    public Optional<User> getUserById(Long id) {
        try {
            Optional<User> queryResponse = userRepository.findById(id);
            return queryResponse;
        } catch (Exception e) {
            throw e;
        }
    }

}
