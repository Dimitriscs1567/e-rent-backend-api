package com.dimanddim.erentbackend.api.v1.services;

import java.util.ArrayList;
import java.util.List;

import com.dimanddim.erentbackend.api.v1.entities.Apartment;

public class SyncApartments {

    private static final String numberSplitter = "Αριθμ. Αγγελίας:&nbsp;";

    private static final String[] keyWords = {"ανακαινισμένο", "ανακαινισμένη",
        "χωρίς κοινόχρηστα", "κλιματιστικό", "ηλιακός", "ατομικό",
        "αυτόνομη", "αυτόνομο", "αυτονομία", "τζάκι", "μπαλκόνι", "κεντρική", "πάρκινγκ",
        "επιπλωμένη", "επιπλωμένο", "parking", "ψυγείο", "κουζίνα", "μπόιλερ",
        "κήπος", "air condition", "air-condition", "ατομική", "γκαράζ", "ισόγεια",
        "ισόγειο", "boiler", "δίχωρη", "δίχωρο", "διαθέσιμο", "πολυκατοικία",
        "τηλέφωνο", "χώρος στάθμευσης", "διαμπερές", "αποθήκη", "κλιματισμό", "μικρό",
        "μικρή", "συσκευές", "επίπλωσης", "βεράντα", "κουφώματα"
    };

    public static List<Apartment> getApartmentsFromHtml(String html) {
        List<Apartment> result = new ArrayList<>();

        String[] apartmentStrings = html.split(numberSplitter);
        for(int i=1; i<apartmentStrings.length; i++){
            String text = apartmentStrings[i];

            Apartment apartment = new Apartment();

            apartment.setId(Long.valueOf(text.split("</b>")[0]));

            apartment.setDate(text.split("<font size=\"-1\"><b>")[1].split("</b>")[0].trim());

            String apartmentAsText = 
                text.split("<div align=\"left\" class=\"smalls\">")[1]
                    .split("<hr size=\"1\" color=\"#0000CC\" noshade>")[0];
            
            if(apartmentAsText.split("<b>").length == 2){
                apartment.setType(apartmentAsText.split("<b>")[1].split("</b>")[0].trim());
            }
            else{
                apartment.setType("Διαμέρισμα");
            }

            if(apartmentAsText.split("τ.μ.").length == 2){
                if(apartment.getType().equals("Διαμέρισμα")){
                    String sqString = apartmentAsText.split("διαμέρισμα")[1].split("τ.μ.")[0].trim();
                    apartment.setSquareMeters(Integer.valueOf(sqString));
                }
                else{
                    String sqString = apartmentAsText.split("</b>")[1].split("τ.μ.")[0].trim();
                    apartment.setSquareMeters(Integer.valueOf(sqString));
                }
            }

            if(getApartmentFloor(apartmentAsText) >= 0){
                apartment.setFloor(getApartmentFloor(apartmentAsText));
            }
            
            if(!getFullAddress(apartmentAsText)[0].isEmpty()){
                apartment.setAddress(getFullAddress(apartmentAsText)[0]);
            }

            if(!getFullAddress(apartmentAsText)[1].isEmpty()){
                apartment.setRegion(getFullAddress(apartmentAsText)[1]);
            }

            if(containsKeyword(apartmentAsText) != null && !containsKeyword(apartmentAsText).isEmpty()){
                String features = "";
                for(String feature : containsKeyword(apartmentAsText)){
                    features += feature + ",";
                }
                apartment.setFeatures(features.substring(0, features.length()-1));
            }

            apartment.setAvailableFrom(apartmentAsText.split("Διαθέσιμο από")[1].split("\\.")[0].trim());

            apartment.setPhones(getPhones(apartmentAsText));

            result.add(apartment);
        }

        return result;
    }

    static private int getApartmentFloor(String text) {
        int floor;

        if(text.split("όροφο").length == 2) {
            String floorString = text.split("όροφο")[0].split("στον")[1].trim();
            if(floorString.contains("πρώτο"))
                floor = 1;
            else if(floorString.contains("δεύτερο"))
                floor = 2;
            else if(floorString.contains("τρίτο"))
                floor = 3;
            else if(floorString.contains("τέταρτο"))
                floor = 4;
            else if(floorString.contains("πέμπτο"))
                floor = 5;
            else if(floorString.contains("έκτο"))
                floor = 6;
            else
                floor = 7;
        }
        else if(text.contains("ισόγειο")){
            floor = 0;
        }
        else{
            floor = -5;
        }

        return floor;
    }

    static private String[] getFullAddress(String text){
        String[] fullAddress = new String[2];

        String address;
        String region;
        if(text.split("οδό").length == 2) {
            String maybeRegion = getRegion(text.split("οδό")[1]);
            if(!maybeRegion.isEmpty()) {
                address = text.split("οδό")[1].split("περιοχή")[0].trim();
                if(address.endsWith(",")) {
                    address = address.substring(0, address.length()-1);
                }
                region = maybeRegion;
            }
            else {
                String[] allAddress = text.split("οδό")[1].split("\\.");
                address = "";
                for(int i=0; i<allAddress.length; i++) {
                    if (containsKeyword(allAddress[i]) == null){
                        address += allAddress[i].trim() + ".";
                    }
                }
                address = address.substring(0, address.length() - 1);
                region = "";
            }
        }
        else {
            address = "";
            region = getRegion(text);
        }

        fullAddress[0] = address.trim();
        fullAddress[1] = region.trim();
        return fullAddress;
    }

    static private String getRegion(String text){

        if(text.split("περιοχή").length == 2) {
            String[] allRegion = text.split("περιοχή")[1].split("\\.");
            String region = "";
            for(int i=0; i<allRegion.length; i++) {
                if(containsKeyword(allRegion[i]) == null){
                    region += allRegion[i].trim() + ".";
                }
            }
            return region.substring(0, region.length() - 1);
        }
        else{
            return "";
        }
    } 

    private static List<String> containsKeyword(String text) {
        boolean contains = false;
        List<String> result = new ArrayList<>();

        for(String word : keyWords) {
            String bWord = String.valueOf(word.charAt(0)).toUpperCase() + word.substring(1);
            if(text.contains(word) || text.contains(bWord)) {
                contains = true;
                switch(word) {
                    case "ανακαινισμένο" :
                    case "ανακαινισμένη" : result.add("Ανακαινισμένο"); break;
                    case "χωρίς κοινόχρηστα" : result.add(bWord); break;
                    case "air condition" :
                    case "air-condition" :
                    case "κλιματισμό" :
                    case "κλιματιστικό" : result.add("Κλιματιστικό"); break;
                    case "αυτόνομη" :
                    case "αυτόνομο" :
                    case "αυτονομία" :
                    case "ατομικό" :
                    case "ατομική" : result.add("Αυτονομία θέρμανσης"); break;
                    case "κεντρική" : result.add("Κεντρική θέρμανση"); break;
                    case "γκαράζ" :
                    case "parking" :
                    case "χώρος στάθμευσης" :
                    case "πάρκινγκ" : result.add("Parking"); break;
                    case "ηλιακός" : result.add(bWord); break;
                    case "κήπος" : result.add(bWord); break;
                    case "μπόιλερ" :
                    case "boiler" : result.add("Boiler"); break;
                    case "επιπλωμένο" :
                    case "επιπλωμένη" :
                    case "επίπλωσης": result.add("Επιπλωμένο"); break;
                    case "βεράντα":
                    case "μπαλκόνι" : result.add(bWord); break;
                    case "διαμπερές" : result.add(bWord); break;
                    case "αποθήκη" : result.add(bWord); break;
                }
            }
        }
        
        return contains ? result : null;
    }

    private static String getPhones(String text) {
        String phones = "";

        String unfilteredPhones = text.split("τηλέφωνο:")[1];
        String phoneParts = "";
        String andWord = "";
        for(int i=0; i<unfilteredPhones.length(); i++){
            boolean acceptable = (unfilteredPhones.charAt(i) >= '0' && unfilteredPhones.charAt(i) <= '9') 
                || unfilteredPhones.charAt(i) == ',' || unfilteredPhones.charAt(i) == '+';

            boolean thereIsAnd = andWord.equals("και ");

            if(thereIsAnd){
                thereIsAnd = false;
                andWord = "";
                phoneParts += ",";
            }
            else{
                if(andWord.isEmpty() && unfilteredPhones.charAt(i) == 'κ'){
                    andWord += unfilteredPhones.charAt(i);
                }
                else if(!andWord.isEmpty()){
                    andWord += unfilteredPhones.charAt(i);
                }

                if(andWord.length() > 4){
                    andWord = "";
                }
            }

            if(acceptable){
                phoneParts += unfilteredPhones.charAt(i);
            }
        }
        
        String[] biggerPhones = phoneParts.split(",");
        for(String phone : biggerPhones){
            if(phone.length() == 10 || phone.startsWith("+")){
                phones += phone + ",";
            }
            else if(phone.length() > 10){
                if(phone.startsWith("6")){
                    phones += phone.substring(0, 10) + ",";
                }
            }
        }

        return phones.substring(0, phones.length() - 1);
    }

}