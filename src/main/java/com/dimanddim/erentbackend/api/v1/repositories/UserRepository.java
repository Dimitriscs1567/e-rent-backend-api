package com.dimanddim.erentbackend.api.v1.repositories;

import java.util.Optional;

import com.dimanddim.erentbackend.api.v1.projections.UserProjection;
import com.dimanddim.erentbackend.api.v1.entities.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(excerptProjection = UserProjection.class)
public interface UserRepository extends CrudRepository<User, Long>{

    Optional<User> findByEmail(String email);
    
}
