package com.nihaoyin.ptsservice.bean;

public class Trace {
    public Position position;
    public boolean loadPoint;
    public boolean unloadPoint;

    public Trace(Position position, boolean loadPoint, boolean unloadPoint){
        this.position = position;
        this.loadPoint = loadPoint;
        this.unloadPoint = unloadPoint;
    }

    @Override
    public String toString() {
        return "Trace{" +
                "position.x=" + position.x +
                ", position.y=" + position.y +
                ", position.z=" + position.z +
                ", loadPoint='" + loadPoint + '\'' +
                ", unloadPoint='" + unloadPoint + '\'' +
                '}';
    }
}
