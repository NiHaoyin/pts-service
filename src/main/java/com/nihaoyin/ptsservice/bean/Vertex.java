package com.nihaoyin.ptsservice.bean;

public class Vertex {
    private final int vertexId;
    private final Position position;


    public Vertex(int vertexId, Position position) {
        this.vertexId = vertexId;
        this.position = position;
    }

    public int getVertexId() { return vertexId; }
    public Position getPosition() { return position; }



    @Override
    public String toString() {
        return "Vertex{" +
                "vertexId='" + vertexId + '\'' +
                ", position.x=" + position.x +
                ", position.y=" + position.y +
                ", position.z=" + position.z +
                '}';
    }
}
