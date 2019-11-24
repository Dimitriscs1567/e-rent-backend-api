package com.dimanddim.erentbackend.api.v1.projections;

import com.dimanddim.erentbackend.api.v1.entities.Apartment;

import org.springframework.data.rest.core.config.Projection;


@Projection(name = "apartmentProjection", types = { Apartment.class })
public interface ApartmentProjection{
    
    Long getId();

    String getDate();

    String getType();

    int getSquareMeters();

    int getFloor();

    String getAddress();

    String getRegion();

    int getPrice();

    String getPhones();

    String getFeatures();

    String getAvailableFrom();
    
}