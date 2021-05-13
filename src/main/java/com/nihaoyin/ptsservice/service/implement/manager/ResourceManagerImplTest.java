package com.nihaoyin.ptsservice.service.implement.manager;

import com.nihaoyin.ptsservice.bean.Order;
import com.nihaoyin.ptsservice.bean.Position;
import com.nihaoyin.ptsservice.bean.Trace;
import com.nihaoyin.ptsservice.service.implement.router.PathPlanning;
import com.nihaoyin.ptsservice.service.interfaces.manager.ResourceManager;

import java.util.List;
import java.util.Queue;

public class ResourceManagerImplTest {
    public static void main(String[] args) {
        ResourceManager resourceManager = ResourceManagerFactory.getResourceManager();
        try{
            resourceManager.init();
        }catch (Exception e){
            e.printStackTrace();
        }
        Queue<Order> waitingOrder = resourceManager.listWaitingOrder();
        while(!waitingOrder.isEmpty()) {
            try {
                List<Order> orders = resourceManager.scheduleOrder();
//                orders = resourceManager.scheduleOrder();
//                orders = resourceManager.scheduleOrder();
                for (Order o : orders) {
                    System.out.println(o);
                }
                if (orders.size() == 1) {
                    Order order = orders.get(0);
                    Position carPosition = resourceManager.getCar(order.getCarId()).getPosition();
                    System.out.println(carPosition.toString());
                    PathPlanning pathPlanning = new PathPlanning(carPosition, order.getSrc(), "", order.getDst(), "");
                    List<Trace> l = pathPlanning.getTraceList();
                } else if (orders.size() == 2) {
                    Order order1 = orders.get(0);
                    Order order2 = orders.get(1);
                    Position carPosition = resourceManager.getCar(order1.getCarId()).getPosition();
                    PathPlanning pathPlanning = new PathPlanning(carPosition, order1.getSrc(), order2.getSrc(), order1.getDst(), order2.getDst());
                    List<Trace> l = pathPlanning.getTraceList();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
