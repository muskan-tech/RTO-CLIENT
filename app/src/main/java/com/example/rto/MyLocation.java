package com.example.rto;

public class MyLocation {
    private double latitude;
    private double longitude;
    public MyLocation(double lat,double longitude){
        this.latitude=lat;
        this.longitude=longitude;
    }
    public MyLocation(){}

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
