package com.dimanddim.erentbackend.api.v1.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dimanddim.erentbackend.api.v1.entities.Apartment;
import com.dimanddim.erentbackend.api.v1.repositories.ApartmentRepository;
import com.dimanddim.erentbackend.api.v1.services.SyncApartments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class SyncApartmentsController {

    @Autowired
    private ApartmentRepository apartmentRepository;

    @GetMapping("/sync_apartments")
    public ResponseEntity syncApartments(){
        Map<Object, Object> response = new HashMap<>();

        try {
            boolean end = false;
            int offset = 0;
            while(!end){
                
                URL url = new URL("http://enoikiazetai.uoi.gr/show.php?offset=" + offset + "&dd_type=0&sort=0");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                int status = con.getResponseCode();
                if(status == 200){
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer html = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        html.append(inputLine);
                    }
                    in.close();
                    con.disconnect();
                    if(html.toString().contains("Δεν υπάρχουν στοιχεία για αυτήν την αναζήτηση")){
                        end = true;
                    }
                    else{
                        List<Apartment> apartments = SyncApartments.getApartmentsFromHtml(html.toString());

                        for(Apartment apartment : apartments){
                            apartmentRepository.save(apartment);
                        }
                    }
                }
                else{
                    response.put("message", "could not connect to uni server.");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }

                offset++;
            }

            response.put("message", "Apartments Synced");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (MalformedURLException e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }   
    }

}