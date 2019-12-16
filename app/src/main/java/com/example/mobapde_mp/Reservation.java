package com.example.mobapde_mp;

public class Reservation {
    private String  ReservationId;
    private String  CInemaID;
    private String  MovieID;
    private String Emial;
    private String Date;

    public Reservation() {
        ;
    }

    public Reservation(String  reservationId, String  CInemaID, String  movieID, String emial, String date) {
        ReservationId = reservationId;
        this.CInemaID = CInemaID;
        MovieID = movieID;
        Emial = emial;
        Date = date;
    }

    public String getReservationId() {
        return ReservationId;
    }

    public void setReservationId(String reservationId) {
        ReservationId = reservationId;
    }

    public String getCInemaID() {
        return CInemaID;
    }

    public void setCInemaID(String CInemaID) {
        this.CInemaID = CInemaID;
    }

    public String getMovieID() {
        return MovieID;
    }

    public void setMovieID(String movieID) {
        MovieID = movieID;
    }

    public String getEmial() {
        return Emial;
    }

    public void setEmial(String emial) {
        Emial = emial;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
