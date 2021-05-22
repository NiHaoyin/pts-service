package com.nihaoyin.ptsservice.service.interfaces.manager;

import com.nihaoyin.ptsservice.bean.Car;
import com.nihaoyin.ptsservice.bean.Order;

import java.util.List;
import java.util.Queue;

public interface OrderManager {
    void pushOrder2WQ(Order order); // 加入新订单到等待队列
    void pushOrder2RL(Order order); // 把这个订单加入到runningList，开始配送
    Order popOrderFromWQ(); // 从等待队列中出队一个优先级最高的订单
    Order popOrderFromWQ(int orderId); // 从等待队列中取出一个特定的订单
    Queue<Order> getWaitingQueue(); // 返回目前的等待队列
    List<Order> getRunningList();
    void popOrderFromRL(int orderId); // 把这个订单从runningList中踢出
    boolean isWaitingQueueEmpty();
    Order getOrder(int orderId); // 根据orderId返回一个特定Order对象
    void changeStatus(int orderId, String newStatus) throws Exception;
}
