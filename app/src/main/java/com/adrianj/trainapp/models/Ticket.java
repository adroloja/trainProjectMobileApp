package com.adrianj.trainapp.models;

public class Ticket {
    private long id;

    private Stops startStops;

    private Stops endStops;

    private Passenger passenger;

    private int seat;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Stops getStartStops() {
        return startStops;
    }

    public void setStartStops(Stops startStops) {
        this.startStops = startStops;
    }

    public Stops getEndStops() {
        return endStops;
    }

    public void setEndStops(Stops endStops) {
        this.endStops = endStops;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", startStops=" + startStops +
                ", endStops=" + endStops +
                ", passenger=" + passenger +
                ", seat=" + seat +
                '}';
    }
}
