package com.nihaoyin.ptsservice.bean;

public class Node {
    private final String nodeId;
    private final Position position;
    private boolean isOccupied;
    private String trayId;
    private final String trayType;
    private final int vertex1;
    private final int vertex2;
    private final String motionType;

    public Node(String nodeId, Position position, boolean isOccupied, String trayId, String trayType,int vertex1,int vertex2,String motionType) {
        this.nodeId = nodeId;
        this.position = position;
        this.isOccupied = isOccupied;
        this.trayId = trayId;
        this.trayType = trayType;
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;

        this.motionType=motionType;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public void setTrayId(String trayId) {
        this.trayId = trayId;
    }

    public String getNodeId(){
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
    public int getVertex1() { return vertex1; }
    public int getVertex2() { return vertex2; }
    public String getMotionType() { return motionType; }

    @Override
    public String toString() {
        return "Node{" +
                "nodeId='" + nodeId + '\'' +
                ", position.x=" + position.x +
                ", position.y=" + position.y +
                ", position.z=" + position.z +
                ", isOccupied=" + isOccupied +
                ", trayId='" + trayId + '\'' +
                ", trayType='" + trayType + '\'' +
                ", vertex1='" + vertex1 + '\'' +
                ", vertex2='" + vertex2 + '\'' +
                ", motionType='" + motionType + '\'' +
                '}';
    }
}
