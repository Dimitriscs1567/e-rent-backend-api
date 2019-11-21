package com.dimanddim.erentbackend.api.v1.projections;

import java.util.List;

import com.dimanddim.erentbackend.api.v1.entities.Apartment;

import org.springframework.data.rest.core.config.Projection;


@Projection(name = "apartmentProjection", types = { Apartment.class })
public interface ApartmentProjection{
    
    Long getId();

    String getDate();

    String getPassword();

    String getType();

    int getSquareMeters();

    int getFloor();

    String getAddress();

    String getRegion();

    int getPrice();

    List<String> getPhones();

    List<String> getFeatures();
    
}