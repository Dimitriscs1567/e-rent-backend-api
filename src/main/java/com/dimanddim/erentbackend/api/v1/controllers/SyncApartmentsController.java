package com.dimanddim.erentbackend.api.v1.controllers;

import java.util.HashMap;
import java.util.Map;

import com.dimanddim.erentbackend.api.v1.services.SyncApartments;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class SyncApartmentsController {

    @PostMapping("/sync_apartments")
    public ResponseEntity syncApartments(@RequestBody Map<String, String> data){
        Map<Object, Object> model = new HashMap<>();

        SyncApartments.getApartmentsFromHtml(data.get("html"));

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

}