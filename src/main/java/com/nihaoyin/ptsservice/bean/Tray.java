package com.nihaoyin.ptsservice.bean;

public class Tray {
    private String trayType;
    private String trayId;

    private String firstCarType;
    private String secondCarType;

    private String status; // 有running和waiting两种状态
    private String nodeId; // 这个Tray目前在哪个位置

    public Tray(String trayType, String trayId, String firstCarType, String secondCarType, String nodeId) {
        this.trayType = trayType;
        this.trayId = trayId;
        this.firstCarType = firstCarType;
        this.secondCarType = secondCarType;
        this.nodeId = nodeId;
    }



    public String getTrayType() {
        return trayType;
    }

    public String getTrayId() {
        return trayId;
    }

    public String getFirstCarType() {
        return firstCarType;
    }

    public String getSecondCarType() {
        return secondCarType;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setTrayType(String trayType) {
        this.trayType = trayType;
    }

    public void setTrayId(String trayId) {
        this.trayId = trayId;
    }

    public void setFirstCarType(String firstCarType) {
        this.firstCarType = firstCarType;
    }

    public void setSecondCarType(String secondCarType) {
        this.secondCarType = secondCarType;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Tray{" +
                "trayType='" + trayType + '\'' +
                ", trayId='" + trayId + '\'' +
                ", firstCarType='" + firstCarType + '\'' +
                ", secondCarType='" + secondCarType + '\'' +
                ", status='" + status + '\'' +
                ", nodeId='" + nodeId + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }
}
