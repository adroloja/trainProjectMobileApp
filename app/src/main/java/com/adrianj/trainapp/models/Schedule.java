package com.adrianj.trainapp.models;

public class Schedule {

    private long id;

    private Train train;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", train=" + train +
                '}';
    }
}
