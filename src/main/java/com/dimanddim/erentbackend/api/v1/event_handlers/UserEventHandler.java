package com.dimanddim.erentbackend.api.v1.event_handlers;

import com.dimanddim.erentbackend.api.v1.entities.User;
import com.dimanddim.erentbackend.api.v1.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@RepositoryEventHandler(User.class)
public class UserEventHandler {

    @Autowired 
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired 
    private UserRepository userRepository;

    @HandleBeforeCreate     
    public void handleUserCreate(User user) throws Exception {
      boolean registeredEmail = userRepository.findByEmail(user.getEmail()).isPresent();
      if(registeredEmail){
        throw new Exception("Email already exists");
      }
  
      user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
}