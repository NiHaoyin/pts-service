package com.nihaoyin.ptsservice.service.implement.router;

import com.nihaoyin.ptsservice.bean.Node;
import com.nihaoyin.ptsservice.bean.Position;

import java.util.ArrayList;

import com.nihaoyin.ptsservice.bean.Trace;
import com.nihaoyin.ptsservice.bean.Vertex;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

public class PathPlanning {
    private Position CarOrigin;
    private String OrderOrigin1;
    private String OrderOrigin2;
    private String OrderDestination1;
    private String OrderDestination2;
    private boolean CarIsShared;
    private List<Trace> TraceList;
    private static List<Node>  NodeList;//读取集配点列表
    private static List<Vertex> VertexList;//读取节点列表


    public List<Trace> getTraceList() throws IOException{
        if(CarIsShared==false){
            int originVertex1=-1;
            int originVertex2=-1;
            int destinationVertex1= -1;
            int destinationVertex2= -1;
            int carVertexId1=-1;
            int carVertexId2=-1;
            String carMotionType="";
            String originMotionType="";
            String destinationMotionType="";
            Position carVertexPosition1=new Position(0,0,0);
            Position carVertexPosition2=new Position(0,0,0);
            Position originVertexPosition1=new Position(0,0,0);
            Position originVertexPosition2=new Position(0,0,0);
            Position destinationVertexPosition1=new Position(0,0,0);
            Position destinationVertexPosition2=new Position(0,0,0);
            Position originPosition=new Position(0,0,0);
            Position destinationPosition=new Position(0,0,0);

            for (Node node:NodeList){
                if(CarOrigin.x==node.getPosition().x&&CarOrigin.y==node.getPosition().y){
                    carVertexId1=node.getVertex1();
                    carVertexId2=node.getVertex2();
                    carMotionType=node.getMotionType();
                }
                if(OrderOrigin1.equals(node.getNodeId())){
                    originPosition=node.getPosition();
                    originVertex1=node.getVertex1();
                    originVertex2=node.getVertex2();
                    originMotionType=node.getMotionType();
                }
                if(OrderDestination1.equals(node.getNodeId())){
                    destinationPosition=node.getPosition();
                    destinationVertex1=node.getVertex1();
                    destinationVertex2=node.getVertex2();
                    destinationMotionType=node.getMotionType();
                }
            }
            Position tempPosition= new Position(0,0,0);
            Trace tempTrace=new Trace(CarOrigin,false,false);
            TraceList.add(tempTrace);
            for (Vertex vertex:VertexList) {
                if (carVertexId1 == vertex.getVertexId()) {
                    carVertexPosition1 = vertex.getPosition();
                }
                if (carVertexId2 == vertex.getVertexId()) {
                    carVertexPosition2 = vertex.getPosition();
                }
                if (originVertex1 == vertex.getVertexId()) {
                    originVertexPosition1 = vertex.getPosition();
                }
                if (originVertex2 == vertex.getVertexId()) {
                    originVertexPosition2 = vertex.getPosition();
                }
                if (destinationVertex1 == vertex.getVertexId()) {
                    destinationVertexPosition1 = vertex.getPosition();
                }
                if (destinationVertex2 == vertex.getVertexId()) {
                    destinationVertexPosition2 = vertex.getPosition();
                }
            }
            int[] minorDist=new int[6];
            int[] majorDist=new int[8];
            minorDist[0] = Math.abs(CarOrigin.x - carVertexPosition1.x) + Math.abs(CarOrigin.y - carVertexPosition1.y);
            minorDist[1] = Math.abs(CarOrigin.x - carVertexPosition2.x) + Math.abs(CarOrigin.y - carVertexPosition2.y);
            minorDist[2] = Math.abs(originPosition.x - originVertexPosition1.x) + Math.abs(originPosition.y - originVertexPosition1.y);
            minorDist[3] = Math.abs(originPosition.x - originVertexPosition2.x) + Math.abs(originPosition.y - originVertexPosition2.y);
            minorDist[4] = Math.abs(destinationPosition.x - destinationVertexPosition1.x) + Math.abs(destinationPosition.y - destinationVertexPosition1.y);
            minorDist[5] = Math.abs(destinationPosition.x - destinationVertexPosition2.x) + Math.abs(destinationPosition.y - destinationVertexPosition2.y);
            Dijkstra D1=new Dijkstra(carVertexId1,originVertex1);
            majorDist[0]=D1.getDistance();
            Dijkstra D2=new Dijkstra(carVertexId1,originVertex2);
            majorDist[1]=D2.getDistance();
            Dijkstra D3=new Dijkstra(carVertexId2,originVertex1);
            majorDist[2]=D3.getDistance();
            Dijkstra D4=new Dijkstra(carVertexId2,originVertex2);
            majorDist[3]=D4.getDistance();
            Dijkstra D5=new Dijkstra(originVertex1,destinationVertex1);
            majorDist[4]=D5.getDistance();
            Dijkstra D6=new Dijkstra(originVertex1,destinationVertex2);
            majorDist[5]=D6.getDistance();
            Dijkstra D7=new Dijkstra(originVertex2,destinationVertex1);
            majorDist[6]=D7.getDistance();
            Dijkstra D8=new Dijkstra(originVertex2,destinationVertex2);
            majorDist[7]=D8.getDistance();
            int DistSum=Integer.MAX_VALUE;
            int[] VertexSet=new int[4];
            for (int i=0;i<2;i++){
                for (int j=0;j<2;j++){
                    for(int k=0;k<2;k++){
                        for (int l=0;l<2;l++){
                            int tempSum=minorDist[i]+minorDist[j+2]+minorDist[k+2]+minorDist[l+4]+majorDist[2*i+j]+majorDist[2*k+l+4];
                            if (tempSum<DistSum){
                                DistSum=tempSum;
                                VertexSet[0]=i; VertexSet[1]=j; VertexSet[2]=k; VertexSet[3]=l;
                            }
                        }
                    }
                }
            }
            switch (2*VertexSet[0]+VertexSet[1]){
                case 0:
                    if(carMotionType.equals("x")){
                        tempPosition= new Position(CarOrigin.x,carVertexPosition1.y,0);
                    }else{
                        tempPosition= new Position(carVertexPosition1.x,CarOrigin.y,0);
                    }
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    int[] Route1=D1.getSinglePath();
                    for (int i=0;i<Route1.length;i++){
//                        System.out.println(Route1[i]);
                        for (Vertex vertex:VertexList){
                            if(Route1[i]==vertex.getVertexId()){
                                tempPosition=vertex.getPosition();
                                tempTrace=new Trace(tempPosition,false,false);
                                TraceList.add(tempTrace);
                                break;
                            }
                        }
                    }
                    if(originMotionType.equals("x")){
                        tempPosition= new Position(originPosition.x,originVertexPosition1.y,0);
                    } else{
                        tempPosition= new Position(originVertexPosition1.x,originPosition.y,0);
                    }
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    tempTrace=new Trace(originPosition,true,false);
                    TraceList.add(tempTrace);
                    break;
                case 1:
                    if(carMotionType.equals("x")){
                        tempPosition= new Position(CarOrigin.x,carVertexPosition1.y,0);
                    }else{
                        tempPosition= new Position(carVertexPosition1.x,CarOrigin.y,0);
                    }
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    int[] Route2=D2.getSinglePath();
                    for (int i=0;i<Route2.length;i++){
//                        System.out.println(Route2[i]);
                        for (Vertex vertex:VertexList){
                            if(Route2[i]==vertex.getVertexId()){
                                tempPosition=vertex.getPosition();
                                tempTrace=new Trace(tempPosition,false,false);
                                TraceList.add(tempTrace);
                                break;
                            }
                        }
                    }
                    if(originMotionType.equals("x")){
                        tempPosition= new Position(originPosition.x,originVertexPosition2.y,0);
                    } else{
                        tempPosition= new Position(originVertexPosition2.x,originPosition.y,0);
                    }
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    tempTrace=new Trace(originPosition,true,false);
                    TraceList.add(tempTrace);
                    break;
                case 2:
                    if(carMotionType.equals("x")){
                        tempPosition= new Position(CarOrigin.x,carVertexPosition2.y,0);
                    }else{
                        tempPosition= new Position(carVertexPosition2.x,CarOrigin.y,0);
                    }
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    int[] Route3=D3.getSinglePath();
                    for (int i=0;i<Route3.length;i++){
                        System.out.println(Route3[i]);
                        for (Vertex vertex:VertexList){
                            if(Route3[i]==vertex.getVertexId()){
                                tempPosition=vertex.getPosition();
                                tempTrace=new Trace(tempPosition,false,false);
                                TraceList.add(tempTrace);
                                break;
                            }
                        }
                    }
                    if(originMotionType.equals("x")){
                        tempPosition= new Position(originPosition.x,originVertexPosition1.y,0);
                    } else{
                        tempPosition= new Position(originVertexPosition1.x,originPosition.y,0);
                    }
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    tempTrace=new Trace(originPosition,true,false);
                    TraceList.add(tempTrace);
                    break;
                default:
                    if(carMotionType.equals("x")){
                        tempPosition= new Position(CarOrigin.x,carVertexPosition2.y,0);
                    }else{
                        tempPosition= new Position(carVertexPosition2.x,CarOrigin.y,0);
                    }
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    int[] Route4=D4.getSinglePath();
                    for (int i=0;i<Route4.length;i++){
//                        System.out.println(Route4[i]);
                        for (Vertex vertex:VertexList){
                            if(Route4[i]==vertex.getVertexId()){
                                tempPosition=vertex.getPosition();
                                tempTrace=new Trace(tempPosition,false,false);
                                TraceList.add(tempTrace);
                                break;
                            }
                        }
                    }
                    if(originMotionType.equals("x")){
                        tempPosition= new Position(originPosition.x,originVertexPosition2.y,0);
                    } else{
                        tempPosition= new Position(originVertexPosition2.x,originPosition.y,0);
                    }
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    tempTrace=new Trace(originPosition,true,false);
                    TraceList.add(tempTrace);
                    break;
            }
            switch (2*VertexSet[2]+VertexSet[3]+4){
                case 4:
                    if(originMotionType.equals("x")){
                        tempPosition= new Position(originPosition.x,originVertexPosition1.y,0);
                    }else{
                        tempPosition= new Position(originVertexPosition1.x,originPosition.y,0);
                    }
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    int[] Route5=D5.getSinglePath();
                    for (int i=0;i<Route5.length;i++){
//                        System.out.println(Route5[i]);
                        for (Vertex vertex:VertexList){
                            if(Route5[i]==vertex.getVertexId()){
                                tempPosition=vertex.getPosition();
                                tempTrace=new Trace(tempPosition,false,false);
                                TraceList.add(tempTrace);
                                break;
                            }
                        }
                    }
                    if(destinationMotionType.equals("x")){
                        tempPosition= new Position(destinationPosition.x,destinationVertexPosition1.y,0);
                    } else{
                        tempPosition= new Position(destinationVertexPosition1.x,destinationPosition.y,0);
                    }
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    tempTrace=new Trace(destinationPosition,false,true);
                    TraceList.add(tempTrace);
                    break;
                case 5:
                    if(originMotionType.equals("x")){
                        tempPosition= new Position(originPosition.x,originVertexPosition1.y,0);
                    }else{
                        tempPosition= new Position(originVertexPosition1.x,originPosition.y,0);
                    }
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    int[] Route6=D6.getSinglePath();
                    for (int i=0;i<Route6.length;i++){
//                        System.out.println(Route6[i]);
                        for (Vertex vertex:VertexList){
                            if(Route6[i]==vertex.getVertexId()){
                                tempPosition=vertex.getPosition();
                                tempTrace=new Trace(tempPosition,false,false);
                                TraceList.add(tempTrace);
                                break;
                            }
                        }
                    }
                    if(destinationMotionType.equals("x")){
                        tempPosition= new Position(destinationPosition.x,destinationVertexPosition2.y,0);
                    } else{
                        tempPosition= new Position(destinationVertexPosition2.x,destinationPosition.y,0);
                    }
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    tempTrace=new Trace(destinationPosition,false,true);
                    TraceList.add(tempTrace);
                    break;
                case 6:
                    if(originMotionType.equals("x")){
                        tempPosition= new Position(originPosition.x,originVertexPosition2.y,0);
                    }else{
                        tempPosition= new Position(originVertexPosition2.x,originPosition.y,0);
                    }
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    int[] Route7=D7.getSinglePath();
                    for (int i=0;i<Route7.length;i++){
//                        System.out.println(Route7[i]);
                        for (Vertex vertex:VertexList){
                            if(Route7[i]==vertex.getVertexId()){
                                tempPosition=vertex.getPosition();
                                tempTrace=new Trace(tempPosition,false,false);
                                TraceList.add(tempTrace);
                                break;
                            }
                        }
                    }
                    if(destinationMotionType.equals("x")){
                        tempPosition= new Position(destinationPosition.x,destinationVertexPosition1.y,0);
                    } else{
                        tempPosition= new Position(destinationVertexPosition1.x,destinationPosition.y,0);
                    }
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    tempTrace=new Trace(destinationPosition,false,true);
                    TraceList.add(tempTrace);
                    break;
                default:
                    if(originMotionType.equals("x")){
                        tempPosition= new Position(originPosition.x,originVertexPosition2.y,0);
                    }else{
                        tempPosition= new Position(originVertexPosition2.x,originPosition.y,0);
                    }
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    int[] Route8=D8.getSinglePath();
                    for (int i=0;i<Route8.length;i++){
//                        System.out.println(Route8[i]);
                        for (Vertex vertex:VertexList){
                            if(Route8[i]==vertex.getVertexId()){
                                tempPosition=vertex.getPosition();
                                tempTrace=new Trace(tempPosition,false,false);
                                TraceList.add(tempTrace);
                                break;
                            }
                        }
                    }
                    if(destinationMotionType.equals("x")){
                        tempPosition= new Position(destinationPosition.x,destinationVertexPosition2.y,0);
                    } else{
                        tempPosition= new Position(destinationVertexPosition2.x,destinationPosition.y,0);
                    }
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    tempTrace=new Trace(destinationPosition,false,true);
                    TraceList.add(tempTrace);
                    break;
            }
        }
        else{
            int origin1Vertex=-1;
            int origin2Vertex=-1;
            int destination1Vertex= -1;
            int destination2Vertex= -1;
            int carVertexId=-1;
            String carMotionType="";
            String origin1MotionType="";
            String origin2MotionType="";
            String destination1MotionType="";
            String destination2MotionType="";
            Position carVertexPosition=new Position(0,0,0);
            Position origin1VertexPosition=new Position(0,0,0);
            Position origin2VertexPosition=new Position(0,0,0);
            Position destination1VertexPosition=new Position(0,0,0);
            Position destination2VertexPosition=new Position(0,0,0);
            Position origin1Position=new Position(0,0,0);
            Position origin2Position=new Position(0,0,0);
            Position destination1Position=new Position(0,0,0);
            Position destination2Position=new Position(0,0,0);

            for (Node node:NodeList){
                if(CarOrigin.x==node.getPosition().x&&CarOrigin.y==node.getPosition().y){
                    carVertexId=node.getVertex1();
                    carMotionType=node.getMotionType();
                }
                if(OrderOrigin1.equals(node.getNodeId())){
                    origin1Position=node.getPosition();
                    origin1Vertex=node.getVertex1();
                    origin1MotionType=node.getMotionType();
                }
                if(OrderOrigin2.equals(node.getNodeId())){
                    origin2Position=node.getPosition();
                    origin2Vertex=node.getVertex1();
                    origin2MotionType=node.getMotionType();
                }
                if(OrderDestination1.equals(node.getNodeId())){
                    destination1Position=node.getPosition();
                    destination1Vertex=node.getVertex1();
                    destination1MotionType=node.getMotionType();
                }
                if(OrderDestination2.equals(node.getNodeId())){
                    destination2Position=node.getPosition();
                    destination2Vertex=node.getVertex1();
                    destination2MotionType=node.getMotionType();
                }
            }
            Position tempPosition= new Position(0,0,0);
            Trace tempTrace=new Trace(CarOrigin,false,false);
            TraceList.add(tempTrace);
            for (Vertex vertex:VertexList) {
                if (carVertexId == vertex.getVertexId()) {
                    carVertexPosition = vertex.getPosition();
                }
                if (origin1Vertex == vertex.getVertexId()) {
                    origin1VertexPosition = vertex.getPosition();
                }
                if (origin2Vertex == vertex.getVertexId()) {
                    origin2VertexPosition = vertex.getPosition();
                }
                if (destination1Vertex == vertex.getVertexId()) {
                    destination1VertexPosition = vertex.getPosition();
                }
                if (destination2Vertex == vertex.getVertexId()) {
                    destination2VertexPosition = vertex.getPosition();
                }
            }
            if(carMotionType.equals("x")){
                tempPosition= new Position(CarOrigin.x,carVertexPosition.y,0);
            }else {
                tempPosition = new Position(carVertexPosition.x, CarOrigin.y, 0);
            }
            tempTrace=new Trace(tempPosition,false,false);
            TraceList.add(tempTrace);

            int[] minorDist=new int[2];
            int[] majorDist=new int[4];
            minorDist[0] = Math.abs(destination1VertexPosition.x - destination1Position.x) + Math.abs(destination1VertexPosition.y - destination1Position.y);
            minorDist[1] = Math.abs(destination2VertexPosition.x - destination2Position.x) + Math.abs(destination2VertexPosition.y - destination2Position.y);
            Dijkstra D1=new Dijkstra(origin1Vertex,destination1Vertex);
            majorDist[0]=D1.getDistance();
            Dijkstra D2=new Dijkstra(origin1Vertex,destination2Vertex);
            majorDist[1]=D2.getDistance();
            Dijkstra D3=new Dijkstra(origin2Vertex,destination1Vertex);
            majorDist[2]=D3.getDistance();
            Dijkstra D4=new Dijkstra(origin2Vertex,destination2Vertex);
            majorDist[3]=D4.getDistance();
            int DistSum=Integer.MAX_VALUE;
            int[] VertexSet=new int[2];
            for(int i=0;i<2;i++){
                for (int j=0;j<2;j++){
                    int tempSum=minorDist[i]+majorDist[2*(1-i)+j];
                    if (tempSum<DistSum){
                        DistSum=tempSum;
                        VertexSet[0]=i; VertexSet[1]=j;
                    }
                }
            }
            if (VertexSet[0]==0){
                Dijkstra D5=new Dijkstra(carVertexId,origin1Vertex);
                Dijkstra D6=new Dijkstra(origin1Vertex,origin2Vertex);
                int[] Route1=D5.getSinglePath();
                for (int i=0;i<Route1.length;i++){
                    System.out.println(Route1[i]);
                    for (Vertex vertex:VertexList){
                        if(Route1[i]==vertex.getVertexId()){
                            tempPosition=vertex.getPosition();
                            tempTrace=new Trace(tempPosition,false,false);
                            TraceList.add(tempTrace);
                            break;
                        }
                    }
                }
                if(origin1MotionType.equals("x")){
                    tempPosition= new Position(origin1Position.x,origin1VertexPosition.y,0);
                }else {
                    tempPosition= new Position(origin1VertexPosition.x,origin1Position.y,0);
                }
                tempTrace=new Trace(tempPosition,false,false);
                TraceList.add(tempTrace);
                tempTrace=new Trace(origin1Position,true,false);
                TraceList.add(tempTrace);
                tempTrace=new Trace(tempPosition,false,false);
                TraceList.add(tempTrace);
                int[] Route2=D6.getSinglePath();
                for (int i=0;i<Route2.length;i++){
                    System.out.println(Route2[i]);
                    for (Vertex vertex:VertexList){
                        if(Route2[i]==vertex.getVertexId()){
                            tempPosition=vertex.getPosition();
                            tempTrace=new Trace(tempPosition,false,false);
                            TraceList.add(tempTrace);
                            break;
                        }
                    }
                }
                if(origin2MotionType.equals("x")){
                    tempPosition= new Position(origin2Position.x,origin2VertexPosition.y,0);
                }else {
                    tempPosition= new Position(origin2VertexPosition.x,origin2Position.y,0);
                }
                tempTrace=new Trace(tempPosition,false,false);
                TraceList.add(tempTrace);
                tempTrace=new Trace(origin2Position,true,false);
                TraceList.add(tempTrace);
                tempTrace=new Trace(tempPosition,false,false);
                TraceList.add(tempTrace);
                if(VertexSet[1]==0){
                    Dijkstra D7=new Dijkstra(destination1Vertex,destination2Vertex);
                    int[] Route3=D3.getSinglePath();
                    for (int i=0;i<Route3.length;i++){
                        System.out.println(Route3[i]);
                        for (Vertex vertex:VertexList){
                            if(Route3[i]==vertex.getVertexId()){
                                tempPosition=vertex.getPosition();
                                tempTrace=new Trace(tempPosition,false,false);
                                TraceList.add(tempTrace);
                                break;
                            }
                        }
                    }
                    if(destination1MotionType.equals("x")){
                        tempPosition= new Position(destination1Position.x,destination1VertexPosition.y,0);
                    }else {
                        tempPosition= new Position(destination1VertexPosition.x,destination1Position.y,0);
                    }
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    tempTrace=new Trace(destination1Position,false,true);
                    TraceList.add(tempTrace);
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    int[] Route4=D7.getSinglePath();
                    for (int i=0;i<Route4.length;i++){
                        System.out.println(Route4[i]);
                        for (Vertex vertex:VertexList){
                            if(Route4[i]==vertex.getVertexId()){
                                tempPosition=vertex.getPosition();
                                tempTrace=new Trace(tempPosition,false,false);
                                TraceList.add(tempTrace);
                                break;
                            }
                        }
                    }
                    if(destination2MotionType.equals("x")){
                        tempPosition= new Position(destination2Position.x,destination2VertexPosition.y,0);
                    }else {
                        tempPosition= new Position(destination2VertexPosition.x,destination2Position.y,0);
                    }
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    tempTrace=new Trace(destination2Position,false,true);
                    TraceList.add(tempTrace);
                }
                else{
                    Dijkstra D7=new Dijkstra(destination2Vertex,destination1Vertex);
                    int[] Route3=D4.getSinglePath();
                    for (int i=0;i<Route3.length;i++){
                        System.out.println(Route3[i]);
                        for (Vertex vertex:VertexList){
                            if(Route3[i]==vertex.getVertexId()){
                                tempPosition=vertex.getPosition();
                                tempTrace=new Trace(tempPosition,false,false);
                                TraceList.add(tempTrace);
                                break;
                            }
                        }
                    }
                    if(destination2MotionType.equals("x")){
                        tempPosition= new Position(destination2Position.x,destination2VertexPosition.y,0);
                    }else {
                        tempPosition= new Position(destination2VertexPosition.x,destination2Position.y,0);
                    }
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    tempTrace=new Trace(destination2Position,false,true);
                    TraceList.add(tempTrace);
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    int[] Route4=D7.getSinglePath();
                    for (int i=0;i<Route4.length;i++){
//                        System.out.println(Route4[i]);
                        for (Vertex vertex:VertexList){
                            if(Route4[i]==vertex.getVertexId()){
                                tempPosition=vertex.getPosition();
                                tempTrace=new Trace(tempPosition,false,false);
                                TraceList.add(tempTrace);
                                break;
                            }
                        }
                    }
                    if(destination1MotionType.equals("x")){
                        tempPosition= new Position(destination1Position.x,destination1VertexPosition.y,0);
                    }else {
                        tempPosition= new Position(destination1VertexPosition.x,destination1Position.y,0);
                    }
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    tempTrace=new Trace(destination1Position,false,true);
                    TraceList.add(tempTrace);
                }
            }
            else{
                Dijkstra D5=new Dijkstra(carVertexId,origin2Vertex);
                Dijkstra D6=new Dijkstra(origin2Vertex,origin1Vertex);
                int[] Route1=D5.getSinglePath();
                for (int i=0;i<Route1.length;i++){
                    System.out.println(Route1[i]);
                    for (Vertex vertex:VertexList){
                        if(Route1[i]==vertex.getVertexId()){
                            tempPosition=vertex.getPosition();
                            tempTrace=new Trace(tempPosition,false,false);
                            TraceList.add(tempTrace);
                            break;
                        }
                    }
                }
                if(origin2MotionType.equals("x")){
                    tempPosition= new Position(origin2Position.x,origin2VertexPosition.y,0);
                }else {
                    tempPosition= new Position(origin2VertexPosition.x,origin2Position.y,0);
                }
                tempTrace=new Trace(tempPosition,false,false);
                TraceList.add(tempTrace);
                tempTrace=new Trace(origin2Position,true,false);
                TraceList.add(tempTrace);
                tempTrace=new Trace(tempPosition,false,false);
                TraceList.add(tempTrace);
                int[] Route2=D6.getSinglePath();
                for (int i=0;i<Route2.length;i++){
//                    System.out.println(Route2[i]);
                    for (Vertex vertex:VertexList){
                        if(Route2[i]==vertex.getVertexId()){
                            tempPosition=vertex.getPosition();
                            tempTrace=new Trace(tempPosition,false,false);
                            TraceList.add(tempTrace);
                            break;
                        }
                    }
                }
                if(origin1MotionType.equals("x")){
                    tempPosition= new Position(origin1Position.x,origin1VertexPosition.y,0);
                }else {
                    tempPosition= new Position(origin1VertexPosition.x,origin1Position.y,0);
                }
                tempTrace=new Trace(tempPosition,false,false);
                TraceList.add(tempTrace);
                tempTrace=new Trace(origin1Position,true,false);
                TraceList.add(tempTrace);
                tempTrace=new Trace(tempPosition,false,false);
                TraceList.add(tempTrace);
                if(VertexSet[1]==0){
                    Dijkstra D7=new Dijkstra(destination1Vertex,destination2Vertex);
                    int[] Route3=D1.getSinglePath();
                    for (int i=0;i<Route3.length;i++){
                        System.out.println(Route3[i]);
                        for (Vertex vertex:VertexList){
                            if(Route3[i]==vertex.getVertexId()){
                                tempPosition=vertex.getPosition();
                                tempTrace=new Trace(tempPosition,false,false);
                                TraceList.add(tempTrace);
                                break;
                            }
                        }
                    }
                    if(destination1MotionType.equals("x")){
                        tempPosition= new Position(destination1Position.x,destination1VertexPosition.y,0);
                    }else {
                        tempPosition= new Position(destination1VertexPosition.x,destination1Position.y,0);
                    }
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    tempTrace=new Trace(destination1Position,false,true);
                    TraceList.add(tempTrace);
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    int[] Route4=D7.getSinglePath();
                    for (int i=0;i<Route4.length;i++){
//                        System.out.println(Route4[i]);
                        for (Vertex vertex:VertexList){
                            if(Route4[i]==vertex.getVertexId()){
                                tempPosition=vertex.getPosition();
                                tempTrace=new Trace(tempPosition,false,false);
                                TraceList.add(tempTrace);
                                break;
                            }
                        }
                    }
                    if(destination2MotionType.equals("x")){
                        tempPosition= new Position(destination2Position.x,destination2VertexPosition.y,0);
                    }else {
                        tempPosition= new Position(destination2VertexPosition.x,destination2Position.y,0);
                    }
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    tempTrace=new Trace(destination2Position,false,true);
                    TraceList.add(tempTrace);
                }
                else{
                    Dijkstra D7=new Dijkstra(destination2Vertex,destination1Vertex);
                    int[] Route3=D2.getSinglePath();
                    for (int i=0;i<Route3.length;i++){
                        System.out.println(Route3[i]);
                        for (Vertex vertex:VertexList){
                            if(Route3[i]==vertex.getVertexId()){
                                tempPosition=vertex.getPosition();
                                tempTrace=new Trace(tempPosition,false,false);
                                TraceList.add(tempTrace);
                                break;
                            }
                        }
                    }
                    if(destination2MotionType.equals("x")){
                        tempPosition= new Position(destination2Position.x,destination2VertexPosition.y,0);
                    }else {
                        tempPosition= new Position(destination2VertexPosition.x,destination2Position.y,0);
                    }
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    tempTrace=new Trace(destination2Position,false,true);
                    TraceList.add(tempTrace);
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    int[] Route4=D7.getSinglePath();
                    for (int i=0;i<Route4.length;i++){
//                        System.out.println(Route4[i]);
                        for (Vertex vertex:VertexList){
                            if(Route4[i]==vertex.getVertexId()){
                                tempPosition=vertex.getPosition();
                                tempTrace=new Trace(tempPosition,false,false);
                                TraceList.add(tempTrace);
                                break;
                            }
                        }
                    }
                    if(destination1MotionType.equals("x")){
                        tempPosition= new Position(destination1Position.x,destination1VertexPosition.y,0);
                    }else {
                        tempPosition= new Position(destination1VertexPosition.x,destination1Position.y,0);
                    }
                    tempTrace=new Trace(tempPosition,false,false);
                    TraceList.add(tempTrace);
                    tempTrace=new Trace(destination1Position,false,true);
                    TraceList.add(tempTrace);
                }
            }
            resetTraceList();
        }
        return TraceList;
    }

    public void resetTraceList(){
        ListIterator<Trace> it = TraceList.listIterator();
        while(it.hasNext()) {
            boolean flag = false;
            Trace tracePrevious = it.next();
            int xPrevious = tracePrevious.position.x;
            int yPrevious = tracePrevious.position.y;
            if (it.hasNext()) {
                Trace traceNow = it.next();
                int xNow = traceNow.position.x;
                int yNow = traceNow.position.y;
                if (it.hasNext()) {
                    Trace traceNext = it.next();
                    int xNext = traceNext.position.x;
                    int yNext = traceNext.position.y;
                    boolean flag1 = (xNow > xPrevious) && (xNow > xNext) && (xNext != xPrevious);
                    boolean flag2 = (xNow < xPrevious) && (xNow < xNext) && (xNext != xPrevious);
                    boolean flag3 = (yNow > yPrevious) && (yNow > yNext) && (yNext != yPrevious);
                    boolean flag4 = (yNow < yPrevious) && (yNow < yNext) && (yNext != yPrevious);
                    flag = flag1 || flag2 || flag3 || flag4;
                    it.previous();
                    it.previous();
                    if (flag) {
                        it.remove();
                        it.previous();
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }
    }

    public static List<Vertex> readVertex() throws IOException
    {
        List<Vertex> VertexSet=new ArrayList<Vertex>();
        XSSFWorkbook VertexXwb = new XSSFWorkbook("src/main/java/com/nihaoyin/ptsservice/config/vertex.xlsx");
        VertexXwb.close();
        XSSFSheet VertexSheet = VertexXwb.getSheetAt(0);
        XSSFRow VertexRow;
        double VertexX;
        double VertexY;
        int VLength= VertexSheet.getPhysicalNumberOfRows()-2;
        for (int i = 1; i <= VLength; i++)
        {
            VertexRow = VertexSheet.getRow(i);
            VertexX =VertexRow.getCell(3).getNumericCellValue();//获取节点x坐标
            VertexY = VertexRow.getCell(4).getNumericCellValue();//获取节点y坐标
            Position VertexPosition= new Position((int)VertexX,(int)VertexY,0);
            Vertex vertex= new Vertex(i-1, VertexPosition);
            VertexSet.add(vertex);
        }
        return VertexSet;
    }

    public static List<Node> readNode() throws IOException
    {
        List<Node> NodeSet=new ArrayList<Node>();
        XSSFWorkbook NodeXwb = new XSSFWorkbook("src/main/java/com/nihaoyin/ptsservice/config/node_1.xlsx");
        NodeXwb.close();
        XSSFSheet NodeSheet = NodeXwb.getSheetAt(0);
        XSSFRow NodeRow;
        String NodeId;
        String NodeType;
        double NodeX;
        double NodeY;
        String NodeState;
        boolean NodeIsOccupied;
        int NodeVertex1;
        int NodeVertex2;
        String NodeMotionType;
        int NLength= NodeSheet.getPhysicalNumberOfRows()-2;
        for (int i = 1; i <= NLength; i++)
        {
            NodeRow = NodeSheet.getRow(i);
            NodeId = NodeRow.getCell(0).getStringCellValue();///获取集配点编号
            NodeType = NodeRow.getCell(1).getStringCellValue();//获取集配点对应托盘类型
            NodeX = NodeRow.getCell(4).getNumericCellValue();//获取集配点x坐标
            NodeY = NodeRow.getCell(5).getNumericCellValue();//获取集配点y坐标
            Position NodePosition= new Position((int)NodeX,(int)NodeY,0);
            NodeState= NodeRow.getCell(6).getStringCellValue();//获取集配点占用情况
            NodeIsOccupied= NodeState.equals("True");
            NodeVertex1=Integer.parseInt(NodeRow.getCell(7).getStringCellValue());//获取集配点对应节点1
            NodeVertex2=Integer.parseInt(NodeRow.getCell(8).getStringCellValue());//获取集配点对应节点2
            NodeMotionType=NodeRow.getCell(9).getStringCellValue();//获取集配点移动方式
            Node node= new Node(NodeId, NodePosition,NodeIsOccupied, "", NodeType,NodeVertex1,NodeVertex2,NodeMotionType);
            NodeSet.add(node);
        }
        return NodeSet;
    }


    public PathPlanning(Position carOrigin,String orderOrigin1,String orderOrigin2,String orderDestination1,String orderDestination2) throws IOException{
        CarOrigin=carOrigin;
        OrderOrigin1=orderOrigin1;
        OrderOrigin2=orderOrigin2;
        OrderDestination1=orderDestination1;
        OrderDestination2=orderDestination2;
        TraceList=new ArrayList<>();

        // 初始化NodeList和VertexList
        if(NodeList==null){
            try{
                NodeList = readNode();
                VertexList = readVertex();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        //判断本订单是否发生拼单
        if (OrderOrigin2.equals("")) {
            CarIsShared=false;
        }else {
            CarIsShared = true;
        }

        //

    }
    public static void main(String[] args) throws IOException{
        Position p= new Position(1091287,432598,0);
        /*PathPlanning test1=new PathPlanning (p,"ZDXZ-024","","ZZ-012","");
        List<Trace> t1= test1.getTraceList();
        for (Trace trace:t1){
            System.out.println(trace.toString());
        }*/
        PathPlanning test2=new PathPlanning (p,"ZDXZ-024","CW-016","ZZ-009","CW-026");

        List<Trace> t2= test2.getTraceList();
        for (Trace trace:t2){
            System.out.println(trace.toString());
        }
        PathPlanning test3=new PathPlanning (p,"ZDXZ-024","CW-016","ZZ-009","CW-026");
        List<Trace> t3= test3.getTraceList();
        for (Trace trace:t3){
            System.out.println(trace.toString());
        }
    }
}
