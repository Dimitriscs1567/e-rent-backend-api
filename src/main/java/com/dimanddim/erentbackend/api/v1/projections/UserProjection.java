package com.dimanddim.erentbackend.api.v1.projections;

import java.util.List;

import com.dimanddim.erentbackend.api.v1.entities.User;

import org.springframework.data.rest.core.config.Projection;


@Projection(name = "userProjection", types = { User.class })
public interface UserProjection{
    
    Long getId();

    String getEmail();

    String getPassword();

    List<String> getRoles();
    
}