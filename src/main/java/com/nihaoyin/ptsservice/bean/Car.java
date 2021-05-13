package com.nihaoyin.ptsservice.bean;

public class Car {
    private String carId;
    private String carType; // 四种取值："PBYSC"/"PBTC1"/"PBTC2"/"CC"
    private int speed;
    private Position position;

    private double load;
    private String status; // 有running和waiting两种状态
    private int capacity; // 取值是1或2

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Car{" +
                "carId='" + carId + '\'' +
                ", carType='" + carType + '\'' +
                ", position=" + position +
                ", status='" + status + '\'' +
                '}';
    }

    public Car(String carId, String carType, int speed, Position position, double load, String status) {
        this.carId = carId;
        this.carType = carType;
        this.speed = speed;
        this.position = position;

        this.load = load;
        this.status = status;
    }

    public Car(){

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
