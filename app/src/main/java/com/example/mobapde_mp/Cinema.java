package com.example.mobapde_mp;

import com.google.android.gms.maps.model.LatLng;

public class Cinema {
    private int CinemaID;
    private String Name;
    private LatLng location;
    public Cinema()
    {
        ;
    }
    public Cinema(int CinemaID,String Name,LatLng location)
    {
        this.CinemaID=CinemaID;
        this.Name= Name;
        this.location=location;
    }

    public int getCinemaID() {
        return CinemaID;
    }

    public void setCinemaID(int cinemaID) {
        CinemaID = cinemaID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
