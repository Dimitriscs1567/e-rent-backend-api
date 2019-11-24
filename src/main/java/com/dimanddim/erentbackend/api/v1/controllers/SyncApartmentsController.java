package com.dimanddim.erentbackend.api.v1.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dimanddim.erentbackend.api.v1.entities.Apartment;
import com.dimanddim.erentbackend.api.v1.repositories.ApartmentRepository;
import com.dimanddim.erentbackend.api.v1.services.SyncApartments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class SyncApartmentsController {

    @Autowired
    private ApartmentRepository apartmentRepository;

    @PostMapping("/sync_apartments")
    public ResponseEntity syncApartments(@RequestBody Map<String, String> data){
        Map<Object, Object> response = new HashMap<>();

        List<Apartment> apartments = SyncApartments.getApartmentsFromHtml(data.get("html"));

        for(Apartment apartment : apartments){
            apartmentRepository.save(apartment);
        }


        response.put("message", "Apartments Synced");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}