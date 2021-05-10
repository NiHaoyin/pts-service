package com.nihaoyin.ptsservice.service.interfaces.manager;

import com.nihaoyin.ptsservice.bean.Node;
import com.nihaoyin.ptsservice.bean.Order;
import com.nihaoyin.ptsservice.bean.Tray;

import java.util.List;
import java.util.Map;
import java.util.Queue;

// ResourceManager只负责管理正在配送和等待中的订单，不管理已完成的订单,
// 已经完成的订单存在mysql数据库中
public interface ResourceManager {
    Queue<Order> listWaitingOrder();
    List<Order> listRunningOrder();
    Order getOrder(int orderId);
    void changeOrderStatus(int orderId, String newStatus) throws Exception;
    void placeOrder(Order newOrder) throws Exception;
    boolean checkParam(Order order) throws Exception;
    void deleteOrder(int orderId) throws Exception;
    void init() throws Exception;

    void printWaitingOrder();

    String getNodeId(String trayId) throws Exception;

    List<String> listAvailableNode(String trayId) throws Exception;
    List<String> listTrayId(String status);
    Tray getTray(String trayId);
    Map<String, Node> getNodeMap();
}
