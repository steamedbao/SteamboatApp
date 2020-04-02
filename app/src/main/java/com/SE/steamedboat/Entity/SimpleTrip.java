package com.SE.steamedboat.Entity;

public class SimpleTrip{

    private String TripName = "NoName";
    private int ID=100000;
    private boolean Ongoing = false;

    public SimpleTrip(){};
    public SimpleTrip(String name, int id, boolean on)
        {TripName = name; ID =id; Ongoing = on;}

    public String getTripName() {
        return TripName;
    }

    public void setTripName(String tripName) {
        TripName = tripName;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public boolean isOngoing() {
        return Ongoing;
    }

    public void setOngoing(boolean ongoing) {
        Ongoing = ongoing;
    }
}