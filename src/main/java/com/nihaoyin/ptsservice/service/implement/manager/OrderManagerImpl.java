package com.nihaoyin.ptsservice.service.implement.manager;

import com.nihaoyin.ptsservice.bean.Order;
import com.nihaoyin.ptsservice.service.interfaces.manager.OrderManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class OrderManagerImpl implements OrderManager {
    private final static Logger logger = LoggerFactory.getLogger(OrderManagerImpl.class);
    private final Queue<Order> waitingQueue = new PriorityQueue<Order>(
            new Comparator<Order>(){
                // priority大的在队首
                @Override
                public int compare(Order o1, Order o2){
                    if(o2.getPriority() > o1.getPriority()){
                        return 1;
                    }else{
                        return -1;
                    }
                }
            }
    );
    private final List<Order> runningList = new ArrayList<Order>();


    public synchronized void pushOrder2WQ(Order order){
        order.setStatus("waiting");
        waitingQueue.add(order);
    }

    public synchronized void pushOrder2RL(Order order){
        order.setStatus("running");
        runningList.add(order);
    }

    public synchronized Order popOrderFromWQ(){
        return waitingQueue.poll();
    }

    public synchronized Order popOrderFromWQ(int orderId){
        for(Order order: waitingQueue){
            if(order.getOrderId() == orderId){
                waitingQueue.remove(order);
                return order;
            }
        }
        return null;
    }

    public synchronized void popOrderFromRL(int orderId){
        for(Order order : runningList){
            if(order.getOrderId() == orderId){
                runningList.remove(order);
                order.setStatus("finished");
            }
        }
    }

    public boolean isWaitingQueueEmpty(){
        return waitingQueue.isEmpty();
    }

    public Order getOrder(int orderId) {
        for(Order order : waitingQueue){
            if(order.getOrderId() == orderId){
                return order;
            }
        }
        for(Order order : runningList){
            if(order.getOrderId() == orderId){
                return order;
            }
        }
        return null;
    }

    public synchronized void changeStatus(int orderId, String newStatus) throws Exception{
        Order order = getOrder(orderId);
        if(order == null){
            throw new Exception("orderId不存在");
        }
        if(order.getStatus().equals(newStatus)){
            throw new Exception("参数错误，该订单已经处于 "+newStatus+" 状态");
        }
        switch (newStatus) {
            case "running":
                waitingQueue.remove(order);
                runningList.add(order);
                order.setStatus("running");
                break;
            case "waiting":
                runningList.remove(order);
                waitingQueue.add(order);
                order.setStatus("waiting");
                break;
            case "finished":
                runningList.remove(order);
                break;
            default:
                throw new Exception("参数错误: " + newStatus + "不存在");
        }
    }


    public Queue<Order> getWaitingQueue(){
        return waitingQueue;
    }

    public void printWaitingQueue(){
        logger.info("\n");
        for(Order order : waitingQueue){
            System.out.println(order.toString());
        }
    }

    public List<Order> getRunningList(){
        return runningList;
    }

    public void printRunningList(){
        logger.info("\n");
        for(Order order : runningList){
           System.out.println(order.toString());
        }
    }

//    public static void main(String[] args) {
//        OrderManager orderManager = new OrderManagerImpl();
//        Random r = new Random(1);
//        orderManager.getWaitingQueue();
//        for(int i = 0; i < 30; i++){
//            orderManager.pushOrder2WQ(new Order(r.nextInt(5)));
//        }
//        System.out.println("///////////////////");
//        orderManager.printWaitingQueue();
//        System.out.println("///////////////////");
//        orderManager.printRunningList();
//        System.out.println("///////////////////");
//
//        for(int i = 0; i < 30; i++){
//            orderManager.pushOrder2RL(orderManager.popOrderFromWQ());
//        }
////        orderManager.printWaitingQueue();
//        System.out.println("///////////////////");
//        orderManager.printRunningList();
//        System.out.println("///////////////////");
//    }
}
