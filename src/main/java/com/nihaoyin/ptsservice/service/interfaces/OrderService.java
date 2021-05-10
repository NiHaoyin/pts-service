package com.nihaoyin.ptsservice.service.interfaces;

import com.nihaoyin.ptsservice.bean.Order;

import java.util.List;
import java.util.Queue;

public interface OrderService {
    int countWaitingOrder();
    int countRunningOrder();
    int countFinishedOrder();
    Order getOrder(int orderId);
    Queue<Order> listWaitingOrder();
    List<Order> listRunningOrder();
    List<Order> listFinishedOrder(int base, int offset);
    void placeOrder(Order newOrder) throws Exception;
    void deleteOrder(int orderId) throws Exception;
    void init() throws Exception;
}
