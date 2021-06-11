package com.nihaoyin.ptsservice.service.interfaces.manager;

import com.nihaoyin.ptsservice.bean.Car;
import com.nihaoyin.ptsservice.bean.Node;
import com.nihaoyin.ptsservice.bean.Order;
import com.nihaoyin.ptsservice.bean.Tray;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Queue;

// ResourceManager只负责管理正在配送和等待中的订单，不管理已完成的订单,
// 已经完成的订单存在mysql数据库中
public interface ResourceManager {
    Queue<Order> listWaitingOrder();
    List<Order> listRunningOrder();
    List<Car> listRunningCar();
    List<Car> listWaitingCar(String carType);
    List<String> listAvailableNode(String trayId) throws Exception;
    List<String> listTrayId(String status);

    Order getOrder(int orderId);
    String getNodeId(String trayId) throws Exception;
    Tray getTray(String trayId);
    Map<String, Node> getNodeMap();
    Car getNearestFreeCar(String carType, String srcNodeId); // 返回距离srcNodeId最近，且车辆类型为carType的车辆
    Car getCar(String carId);

    void changeCarStatus(String carId, String newStatus) throws Exception;
    void changeOrderStatus(int orderId, String newStatus) throws Exception;
    void placeOrder(Order newOrder) throws Exception;
    void runOrder(Order order, Car car) throws Exception;
    void finishOrder(int orderId, String carId) throws Exception;
    List<Order> scheduleOrder() throws Exception;

    boolean checkParam(Order order) throws Exception;
    void deleteOrder(int orderId) throws Exception;
    void init() throws Exception;
    void reset() throws Exception;
}
