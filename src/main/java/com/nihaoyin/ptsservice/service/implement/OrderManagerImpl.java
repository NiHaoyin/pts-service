package com.nihaoyin.ptsservice.service.implement;

import com.nihaoyin.ptsservice.bean.Order;
import com.nihaoyin.ptsservice.bean.Tray;
import com.nihaoyin.ptsservice.service.interfaces.OrderManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class OrderManagerImpl implements OrderManager {
    private final static Logger logger = LoggerFactory.getLogger(OrderManagerImpl.class);
    private Queue<Order> waitingQueue = new PriorityQueue<Order>(
            new Comparator<Order>(){
                // priority大的在队首
                @Override
                public int compare(Order o1, Order o2){
                    return o2.getPriority() - o1.getPriority();
                }
            }
    );
    private List<Order> runningList = new ArrayList<Order>();

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

    public void changeStatus(int orderId, String newStatus) throws Exception{
        Order order = getOrder(orderId);
        if(order.getStatus().equals(newStatus)){
            throw new Exception("参数错误，该托盘已经处于 "+newStatus+" 状态");
        }
        if(newStatus.equals("running")){
            waitingQueue.remove(order);
            runningList.add(order);
            order.setStatus("running");
        }else if(newStatus.equals("waiting")){
            runningList.remove(order);
            waitingQueue.add(order);
            order.setStatus("waiting");
        }else{
            throw new Exception("参数错误: "+newStatus+"不合法");
        }
    }


    public Queue<Order> getWaitingQueue(){
        return waitingQueue;
    }

    public void printWaitingQueue(){
        logger.info("\n");
        for(Order order : waitingQueue){
            logger.info("WaitingQueue: {}", order.toString());
            logger.info("\n");
        }
    }

    public List<Order> getRunningList(){
        return runningList;
    }

    public void printRunningList(){
        logger.info("\n");
        for(Order order : runningList){
            logger.info("RunningList: {}", order.toString());
            logger.info("\n");
        }
    }

    // 集配初始化。集配场景一开始会随机生成30条订单加入waitingList
    public void init(){

    }

    public static void main(String[] args) {
        OrderManager orderManager = new OrderManagerImpl();
        Random r = new Random(1);
        for(int i = 0; i < 30; i++){
            orderManager.pushOrder2WQ(new Order(r.nextInt(5)));
        }
        System.out.println("///////////////////");
        orderManager.printWaitingQueue();
        System.out.println("///////////////////");
        orderManager.printRunningList();
        System.out.println("///////////////////");

        for(int i = 0; i < 30; i++){
            orderManager.pushOrder2RL(orderManager.popOrderFromWQ());
        }
//        orderManager.printWaitingQueue();
        System.out.println("///////////////////");
        orderManager.printRunningList();
        System.out.println("///////////////////");
    }
}
