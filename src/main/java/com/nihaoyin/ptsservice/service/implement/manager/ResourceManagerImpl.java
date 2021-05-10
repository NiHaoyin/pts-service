package com.nihaoyin.ptsservice.service.implement.manager;

import com.nihaoyin.ptsservice.bean.Node;
import com.nihaoyin.ptsservice.bean.Order;
import com.nihaoyin.ptsservice.bean.Tray;
import com.nihaoyin.ptsservice.service.implement.manager.CarManagerImpl;
import com.nihaoyin.ptsservice.service.implement.manager.NodeManagerImpl;
import com.nihaoyin.ptsservice.service.implement.manager.OrderManagerImpl;
import com.nihaoyin.ptsservice.service.interfaces.manager.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class ResourceManagerImpl implements ResourceManager {
    private final CarManager carManager = new CarManagerImpl();
    private final OrderManager orderManager = new OrderManagerImpl();
    private final NodeManager nodeManager = new NodeManagerImpl();
    private final TrayManager trayManager = new TrayManagerImpl();
    private final static Logger logger = LoggerFactory.getLogger(ResourceManagerImpl.class);

    public ResourceManagerImpl() throws IOException {
    }

    @Override
    public Queue<Order> listWaitingOrder() {
        return orderManager.getWaitingQueue();
    }

    @Override
    public List<Order> listRunningOrder() {
        return orderManager.getRunningList();
    }

    @Override
    public Order getOrder(int orderId) {
        return orderManager.getOrder(orderId);
    }


    @Override
    public void changeOrderStatus(int orderId, String newStatus) throws Exception {
        orderManager.changeStatus(orderId, newStatus);
    }

    // 1 检查order状态是否为waiting
    // 2 将order从waitinglist中删除
    // 3 把托盘从dst移走，放到src
    // 4 修改dst和src的状态
    // 5 复原托盘状态
    public synchronized void deleteOrder(int orderId) throws Exception{
        Order order = orderManager.getOrder(orderId);
        if(order == null){
            throw new Exception("该orderId不存在或者已完成");
        }
        if(!order.getStatus().equals("waiting")){
            throw new Exception("该订单不能被删除");
        }
        String trayId = order.getTrayId();
        String src = order.getSrc();
        String dst = order.getDst();
        orderManager.popOrderFromWQ(orderId);
        nodeManager.placeTray(src, trayId);
        nodeManager.removeTray(dst);
        trayManager.changeStatus(trayId, "waiting");
    }

    // 1 校验参数
    // 2 订单加入等待队列
    // 3 修改起点集配点状态
    // 4 修改终点集配点状态
    // 5 修改托盘状态和位置
    public synchronized void placeOrder(Order newOrder) throws Exception {
        if(checkParam(newOrder)){
            newOrder.setCarId("");
            newOrder.setCreated(new Date().toString());
            orderManager.pushOrder2WQ(newOrder);
            nodeManager.removeTray(newOrder.getSrc());
            nodeManager.placeTray(newOrder.getDst(), newOrder.getTrayId());
            trayManager.changeStatus(newOrder.getTrayId(), "running");
            trayManager.moveTray(newOrder.getTrayId(), newOrder.getDst());
            logger.info("成功下单 {}", newOrder.toString());
        }else{
            logger.info("订单参数错误 {}", newOrder.toString());
            throw new Exception("订单参数错误");
        }
    }

    // 1 起始点必须存放着对应托盘
    // 2 终点必须不存放托盘，而且终点对应的托盘类型要匹配
    // 3 起点和终点必须不同
    // 4 trayId, src, dst参数不为空且要存在
    // 5 当前托盘必须处于waiting状态
    public synchronized boolean checkParam(Order order) throws Exception {
        Node src = nodeManager.getNode(order.getSrc());
        Node dst = nodeManager.getNode(order.getDst());
        Tray tray = trayManager.getTray(order.getTrayId());
        if(!tray.getStatus().equals("waiting")){
            return false;
        }
        if(src == null){
            return false;
        }
        if(dst == null){
            return false;
        }
        if(src.getNodeId().equals(dst.getNodeId())){
            return false;
        }
        // 起始点必须存放着对应托盘
        String trayId = nodeManager.getTrayId(src.getNodeId());
        if(!trayId.equals(order.getTrayId())){
            return false;
        }
        // 终点必须不存放托盘
        if(dst.isOccupied()){
            return false;
        }
        // 起点和终点必须具有一致的托盘类型
        if(!dst.getTrayType().equals(src.getTrayType())){
            return false;
        }
        return true;
    }

    public void init() throws Exception {
        Map<String, Node> nodeMap = nodeManager.getNodeMap();
        Random r = new Random(1);
        int orderId = 1000;
        for (Map.Entry<String, Node> entry : nodeMap.entrySet()) {
            Node node = entry.getValue();
            if(orderId > 1030){
                break;
            }
            if (node.isOccupied()) {
                String src = node.getNodeId();
                String trayId = node.getTrayId();
                for (Map.Entry<String, Node> x : nodeMap.entrySet()) {
                    Node dstNode = x.getValue();
                    if (!dstNode.isOccupied() && (dstNode.getTrayType().equals(node.getTrayType()))) {
                        String dst = dstNode.getNodeId();
                        Order order = new Order(src, dst, trayId, 0, r.nextInt(3)+1, 1);
                            placeOrder(order);
                            order.setOrderId(orderId++);
                        break;
                    }
                }
            }
        }
    }

    public void printWaitingOrder(){
        Queue<Order> waitingOrder = orderManager.getWaitingQueue();
        int s = waitingOrder.size();
        for(int i = 0; i < s; i++){
            System.out.println(waitingOrder.poll().getPriority());
        }
    }

    @Override
    public String getNodeId(String trayId) throws Exception{
        Tray t = trayManager.getTray(trayId);
        if(t == null){
            throw new Exception("该trayId不存在");
        }
        return t.getNodeId();
    }

    @Override
    public List<String> listAvailableNode(String trayId) throws Exception {
        Tray t = trayManager.getTray(trayId);
        if(t == null){
            throw new Exception("该trayId不存在");
        }
        String trayType = t.getTrayType();
        return nodeManager.listUnoccupiedNodes(trayType);
    }

    @Override
    public List<String> listTrayId(String status) {
        List<String> trayIdList = new ArrayList<String>();
        if(status.equals("running")){
            List<Tray> runningTray = trayManager.listRunningTray();
            for(Tray t: runningTray){
                trayIdList.add(t.getTrayId());
            }
        }else if(status.equals("waiting")){
            List<Tray> waitingTray = trayManager.listWaitingTray();
            for(Tray t: waitingTray){
                trayIdList.add(t.getTrayId());
            }
        }
        return trayIdList;
    }

    @Override
    public Tray getTray(String trayId) {
        return trayManager.getTray(trayId);
    }

    @Override
    public Map<String, Node> getNodeMap() {
        return nodeManager.getNodeMap();
    }

}
