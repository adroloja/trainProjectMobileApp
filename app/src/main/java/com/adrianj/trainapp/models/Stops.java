package com.adrianj.trainapp.models;

import java.util.Date;

public class Stops {

    private long id;

    private Station stationStop;

    private Train trainStops;

    private Schedule schedule;

    private Date time;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Station getStationStop() {
        return stationStop;
    }

    public void setStationStop(Station stationStop) {
        this.stationStop = stationStop;
    }

    public Train getTrainStops() {
        return trainStops;
    }

    public void setTrainStops(Train trainStops) {
        this.trainStops = trainStops;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Stops{" +
                "id=" + id +
                ", stationStop=" + stationStop +
                ", trainStops=" + trainStops +
                ", schedule=" + schedule +
                ", time=" + time +
                '}';
    }
}
