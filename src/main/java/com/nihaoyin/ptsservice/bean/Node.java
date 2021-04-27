package com.nihaoyin.ptsservice.bean;

public class Node {
    private final String nodeId;
    private final Position position;
    private boolean isOccupied;
    private String trayId;
    private final String trayType;

    public Node(String nodeId, Position position, boolean isOccupied, String trayId, String trayType) {
        this.nodeId = nodeId;
        this.position = position;
        this.isOccupied = isOccupied;
        this.trayId = trayId;
        this.trayType = trayType;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public void setTrayId(String trayId) {
        this.trayId = trayId;
    }

    public String getId(){
        return nodeId;
    }
    public Position getPosition(){
        return position;
    }
    public boolean isOccupied(){
        return isOccupied;
    }
    public String getTrayId() {
        return trayId;
    }
    public String getTrayType() {
        return trayType;
    }

    @Override
    public String toString() {
        return "Node{" +
                "nodeId='" + nodeId + '\'' +
                ", position=" + position +
                ", isOccupied=" + isOccupied +
                ", trayId='" + trayId + '\'' +
                ", trayType='" + trayType + '\'' +
                '}';
    }
}
