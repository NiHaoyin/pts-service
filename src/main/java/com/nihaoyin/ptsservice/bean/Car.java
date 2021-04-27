package com.nihaoyin.ptsservice.bean;

public class Car {
    private String carId;
    private String carType;
    private int speed;
    private Position position;
    private double load;
    private String status; // 有running和waiting两种状态

    public Car(String carId, String carType, int speed, Position position, double load, String status) {
        this.carId = carId;
        this.carType = carType;
        this.speed = speed;
        this.position = position;

        this.load = load;
        this.status = status;
    }

    public String getCarId() {
        return carId;
    }

    public String getCarType() {
        return carType;
    }

    public int getSpeed() {
        return speed;
    }

    public Position getPosition() {
        return position;
    }

    public double getLoad() {
        return load;
    }

    public String getStatus() {
        return status;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setLoad(double load) {
        this.load = load;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
