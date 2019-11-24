package com.dimanddim.erentbackend.api.v1.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "apartments")
public class Apartment {

    @Id
    private long id;

    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "squareMeters")
    private int squareMeters;

    @Column(name = "floor")
    private int floor;

    @Column(name = "address")
    private String address;

    @Column(name = "region")
    private String region;

    @Column(name = "phones", nullable = false)
    private String phones;

    @Column(name = "price")
    private int price;

    @Column(name = "features")
    private String features;

    @Column(name = "availableFrom")
    private String availableFrom;

    public Apartment() {

    }

    public Apartment(long id, String date, String type, int squareMeters, int floor, String address, String region,
            String phones, int price, String features) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.squareMeters = squareMeters;
        this.floor = floor;
        this.address = address;
        this.region = region;
        this.phones = phones;
        this.price = price;
        this.features = features;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSquareMeters() {
        return squareMeters;
    }

    public void setSquareMeters(int squareMeters) {
        this.squareMeters = squareMeters;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(String availableFrom) {
        this.availableFrom = availableFrom;
    }
    
}