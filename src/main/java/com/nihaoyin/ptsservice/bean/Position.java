package com.nihaoyin.ptsservice.bean;

import javafx.geometry.Pos;

public class Position {
    public int x;
    public int y;
    public int z;
    public Position(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public boolean equals(Position p){
        return p.x == this.x && p.y == this.y;
    }
}
