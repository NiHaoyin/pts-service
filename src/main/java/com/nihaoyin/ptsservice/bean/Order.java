package com.nihaoyin.ptsservice.bean;

public class Order {
    private String src; // 起点(nodeId)
    private String dst; // 终点(nodeId)
    private String trayId;
    private int orderId; // 只有orderId和userId是int类型，其他Id都是String类型
    private int priority;
    private int userId;
    private String carId;
    private String carType;
    private String status;

    public Order(int priority) {
        this.priority = priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    public void setTrayId(String trayId) {
        this.trayId = trayId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getSrc() {
        return src;
    }

    public String getDst() {
        return dst;
    }

    public String getTrayId() {
        return trayId;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getUserId() {
        return userId;
    }

    public String getCarId() {
        return carId;
    }

    public String getCarType() {
        return carType;
    }

    public int getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "src='" + src + '\'' +
                ", dst='" + dst + '\'' +
                ", trayId='" + trayId + '\'' +
                ", orderId=" + orderId +
                ", priority=" + priority +
                ", userId=" + userId +
                ", carId='" + carId + '\'' +
                ", carType='" + carType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
