package com.dimanddim.erentbackend.api.v1.repositories;

import com.dimanddim.erentbackend.api.v1.projections.ApartmentProjection;
import com.dimanddim.erentbackend.api.v1.entities.Apartment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(excerptProjection = ApartmentProjection.class)
public interface ApartmentRepository extends CrudRepository<Apartment, Long>{
    
}