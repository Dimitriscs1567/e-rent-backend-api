package com.dimanddim.erentbackend.api.v1.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dimanddim.erentbackend.api.v1.entities.User;
import com.dimanddim.erentbackend.api.v1.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RepositoryRestController
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PatchMapping("/users/{id}")
    public ResponseEntity<Object> updateFact(@PathVariable Long id, @RequestBody Map<String, Object> data) {

        Map<String, String> response = new HashMap<>();

        if (!userRepository.findById(id).isPresent()) {
            response.put("message", "Unable to find user with id: " + id);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findById(id).get();

        if (data.get("roles") != null) {
            user.setRoles((List<String>) data.get("roles"));
        }

        if (data.get("password") != null && !data.get("password").equals("")) {
            user.setPassword(passwordEncoder.encode((String) data.get("password")));
        }

        if (data.get("email") != null) {
            user.setEmail((String) data.get("email"));
        }

        userRepository.save(user);

        response.put("email", user.getEmail());
        response.put("password", user.getPassword());
        response.put("roles", user.getRoles().toString());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
