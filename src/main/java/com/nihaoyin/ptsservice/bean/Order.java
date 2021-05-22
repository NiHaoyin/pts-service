package com.nihaoyin.ptsservice.bean;

import java.util.Date;

public class Order {
    private String src; // 起点(nodeId)
    private String dst; // 终点(nodeId)
    private String trayId; // 运送的托盘Id
    private int orderId;  // 主键
    private int priority; // 优先级
    private int userId; // 下单人的用户Id
    private String carId; // 匹配的运输车Id，在没匹配之前该属性为null
    private String carType; // 匹配的运输车类型，在没匹配之前该属性为null
    private String status; // 状态，有waiting, running, finished三种取值

    private String created;
    private boolean isDeleted;

    public Order(int priority) {
        this.priority = priority;
    }
    public Order(String src, String dst, String trayId, int orderId, int priority, int userId){
        this.src = src;
        this.dst = dst;
        this.trayId = trayId;
        this.orderId = orderId;
        this.priority = priority;
        this.userId = userId;
    }
    public Order(){}
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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isDeleted() {
        return isDeleted;
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

    public Order(int orderId, int userId, String trayId, String src, String dst,
                 int priority, String carId, boolean isDeleted, boolean isFinished, Date created) {
        this.src = src;
        this.dst = dst;
        this.trayId = trayId;
        this.orderId = orderId;
        this.priority = priority;
        this.userId = userId;
        this.carId = carId;
//        this.carType = carType;
        if(isFinished){
            this.status = "finished";
        }else{
            this.status = "waiting";
        }
        this.created = created.toString();
        this.isDeleted = isDeleted;
    }
}
