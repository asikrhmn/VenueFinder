package com.nordea.venues.entities;

public class Venue {

    private String name = "";
    private String address = "";
    private int distance = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDistance() {
        return distance;
    }

    public String getDistanceAsString() {
        return distance + " m";
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
