package com.dimanddim.erentbackend.api.v1.services;

import java.util.ArrayList;
import java.util.List;

import com.dimanddim.erentbackend.api.v1.entities.Apartment;

public class SyncApartments {
    private static final String numberSplitter = "Αριθμ. Αγγελίας:&nbsp;";

    public static List<Apartment> getApartmentsFromHtml(String html) {
        List<Apartment> result = new ArrayList<>();

        String[] apartmentStrings = html.split(numberSplitter);
        for(int i=1; i<apartmentStrings.length; i++){
            String text = apartmentStrings[i];

            long id = Long.valueOf(text.split("</b>")[0]);

            String date = text.split("<font size=\"-1\"><b>")[1].split("</b>")[0];

            String apartmentAsText = text.split("<div align=\"left\" class=\"smalls\">")[1].split("<hr size=\"1\" color=\"#0000CC\" noshade>")[0];
            
            String type;
            if(apartmentAsText.split("<b>").length == 2){
                type = apartmentAsText.split("<b>")[1].split("</b>")[0];
            }
            else{
                type = "Διαμέρισμα";
            }

            System.out.println(id);
            System.out.println(date);
            System.out.println(type);

        }

        return result;
    }
}