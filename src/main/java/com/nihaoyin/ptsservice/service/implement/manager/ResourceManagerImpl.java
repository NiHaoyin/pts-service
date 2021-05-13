package com.nihaoyin.ptsservice.service.implement.manager;

import com.nihaoyin.ptsservice.bean.*;
import com.nihaoyin.ptsservice.service.implement.manager.CarManagerImpl;
import com.nihaoyin.ptsservice.service.implement.manager.NodeManagerImpl;
import com.nihaoyin.ptsservice.service.implement.manager.OrderManagerImpl;
import com.nihaoyin.ptsservice.service.implement.router.PathPlanning;
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

    public void changeOrderStatus(int orderId, String newStatus) throws Exception {
        orderManager.changeStatus(orderId, newStatus);
    }

    public void changeCarStatus(String carId, String newStatus) throws Exception {
        carManager.changeCarStatus(carId, newStatus);
    }

    public List<Order> scheduleOrder() throws Exception {
        Queue<Order> waitingOrderList = listWaitingOrder();
        List<Order> ret = new ArrayList<Order>();
        if(waitingOrderList.isEmpty()){
            return ret;
        }
        Order order = waitingOrderList.poll();
        String trayId = order.getTrayId();
        String firstCarType = trayManager.getTray(trayId).getFirstCarType();
        String secondCarType = trayManager.getTray(trayId).getSecondCarType();
        Car freeCar = null;
        switch (firstCarType){
            case "PBYSC":
                freeCar = getNearestFreeCar("PBYSC", order.getSrc());
                if(freeCar != null){
                    logger.info("\n指派 {} \n配送订单 {} ",freeCar.toString(), order.toString());
                    runOrder(order, freeCar);
                    ret.add(order);
                    return ret;
                }else{
                    freeCar = getNearestFreeCar(secondCarType, order.getSrc());
                    if(freeCar != null){
                        logger.info("\n指派 {} \n配送订单 {} ",freeCar.toString(), order.toString());
                        runOrder(order, freeCar);
                        ret.add(order);
                        return ret;
                    }else{
                        logger.info("目前没有空余车辆, 订单重新插入等待队列 {}", order.toString());
                        int priority = order.getPriority();
                        if(priority > 1){
                            order.setPriority(priority-1);
                        }
                        waitingOrderList.add(order);
                        return ret;
                    }
                }
            case "PBTC":
                freeCar = getNearestFreeCar("PBTC1", order.getSrc());
                if(freeCar != null){
                    logger.info("\n指派 {} \n配送订单 {} ",freeCar.toString(), order.toString());
                    runOrder(order, freeCar);
                    ret.add(order);
                    return ret;
                }else{
                    freeCar = getNearestFreeCar("PBTC2", order.getSrc());
                    if(freeCar != null){
                        Order order2 = null;
                        for(Order o: waitingOrderList){
                            String carType1 = trayManager.getTray(o.getTrayId()).getFirstCarType();
                            if(carType1.equals("PBTC")){
                                order2 = o;
                                waitingOrderList.remove(o);
                                break;
                            }
                        }
                        logger.info("\n指派 {} \n配送订单 {} ",freeCar.toString(), order.toString());
                        runOrder(order, freeCar);
                        if(order2 == null){
                            ret.add(order);
                        }else{
                            logger.info("\n指派 {} \n配送订单 {} ",freeCar.toString(), order2.toString());
                            runOrder(order2, freeCar);
                            ret.add(order);
                            ret.add(order2);
                        }
                        return ret;
                    }else{
                        freeCar = getNearestFreeCar(secondCarType, order.getSrc());
                        if(freeCar != null){
                            logger.info("\n指派 {} \n配送订单 {} ",freeCar.toString(), order.toString());
                            runOrder(order, freeCar);
                            ret.add(order);
                        }else{
                            logger.info("目前没有空余车辆，订单重新插入等待队列 {}", order.toString());
                            int priority = order.getPriority();
                            if(priority > 1){
                                order.setPriority(priority-1);
                            }
                            waitingOrderList.add(order);
                        }
                        return ret;
                    }
                }
            case "CC":
                freeCar = getNearestFreeCar("CC", order.getSrc());
                if(freeCar != null){
                    logger.info("\n指派 {} \n配送订单 {} ",freeCar.toString(), order.toString());
                    runOrder(order, freeCar);
                    ret.add(order);
                    return ret;
                }else{
                    freeCar = getNearestFreeCar(secondCarType, order.getSrc());
                    if(freeCar != null){
                        logger.info("\n指派 {} \n配送订单 {} ",freeCar.toString(), order.toString());
                        runOrder(order, freeCar);
                        ret.add(order);
                    }else{
                        logger.info("目前没有空余车辆, 订单重新插入等待队列 {}", order.toString());
                        int priority = order.getPriority();
                        if(priority > 1){
                            order.setPriority(priority-1);
                        }
                        waitingOrderList.add(order);
                    }
                    return ret;
                }
        }
        return ret;
    }

    // 1 订单状态改为running
    // 2 车辆状态改为running
    // 3 设置订单的car相关属性
    // 4 修改托盘位置和状态
    // 5 修改起点状态
    public synchronized void runOrder(Order order, Car car)throws Exception{
        order.setStatus("running");
        orderManager.pushOrder2RL(order);
        changeCarStatus(car.getCarId(), "running");
        order.setCarId(car.getCarId());
        order.setCarType(car.getCarType());
        Tray tray = trayManager.getTray(order.getTrayId());
        tray.setNodeId(car.getCarId());
        tray.setStatus("running");
        nodeManager.getNode(order.getSrc()).setOccupied(false);
        logger.info("\n 开始运送订单 {}", order.toString());
    }

    // 1 订单状态改为finished
    // 2 车辆状态改为waiting
    // 3 托盘状态改为noOrder
    // 4 托盘位置改为终点
    // 5 终点放上托盘
    public void finishOrder(int orderId, String carId) throws Exception {
        Order order = orderManager.getOrder(orderId);
        logger.info("\n 完成订单 {}", order.toString());
        changeOrderStatus(orderId, "finished");
        changeCarStatus(carId, "waiting");
        Tray tray = trayManager.getTray(order.getTrayId());
        tray.setNodeId(order.getDst());
        tray.setStatus("noOrder");
        nodeManager.placeTray(order.getDst(), order.getTrayId());
    }

    // 1 检查order状态是否为waiting
    // 2 将order从waitinglist中删除
    // 3 修改终点状态
    // 4 修改托盘状态
    public synchronized void deleteOrder(int orderId) throws Exception{
        Order order = orderManager.getOrder(orderId);
        if(order == null){
            throw new Exception("该order不存在或者已完成");
        }
        logger.info("\n 删除订单 {}", order.toString());
        if(!order.getStatus().equals("waiting")){
            throw new Exception("该订单不能被删除");
        }
        String trayId = order.getTrayId();
        String dst = order.getDst();
        orderManager.popOrderFromWQ(orderId);
        nodeManager.getNode(dst).setOccupied(false);
        trayManager.changeStatus(trayId, "noOrder");
    }

    // 1 校验参数
    // 2 订单加入等待队列
    // 3 修改终点集配点状态
    // 4 修改托盘状态
    public synchronized void placeOrder(Order newOrder) throws Exception {
        if(checkParam(newOrder)){
            newOrder.setCarId("");
            newOrder.setCreated(new Date().toString());
            orderManager.pushOrder2WQ(newOrder);
//            nodeManager.removeTray(newOrder.getSrc());
//            nodeManager.placeTray(newOrder.getDst(), newOrder.getTrayId());
            Node n = nodeManager.getNode(newOrder.getDst());
            n.setOccupied(true);
            trayManager.changeStatus(newOrder.getTrayId(), "waiting");
//            trayManager.moveTray(newOrder.getTrayId(), newOrder.getDst());
            logger.info("\n成功下单 {}", newOrder.toString());
        }else{
            logger.info("\n订单参数错误 {}", newOrder.toString());
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
        if(!tray.getStatus().equals("noOrder")){
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
        // 终点必须不被占用
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
        // 运输车开到最近的集配点
        List<Car> carList = carManager.listWaitingCar("all");
        List<String> occupiedNodes = new ArrayList<>();
        for(Car car: carList){
            Position carPosition = car.getPosition();
            Node nearestNode = null;
            for (Map.Entry<String, Node> entry : nodeMap.entrySet()) {
                int min = Integer.MAX_VALUE;
                Position nodePosition = entry.getValue().getPosition();
                Node node = entry.getValue();
                int distance = Math.abs(carPosition.x - nodePosition.x) + Math.abs(carPosition.y - nodePosition.y);
                if(distance < min && !occupiedNodes.contains(node.getNodeId())){
                    min = distance;
                    nearestNode = entry.getValue();
                }
            }
            assert nearestNode != null;
            car.setPosition(nearestNode.getPosition());
            occupiedNodes.add(nearestNode.getNodeId());
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
        List<Tray> trayList = trayManager.listTray(status);
        for(Tray t: trayList){
            trayIdList.add(t.getTrayId());
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

    // 1 得到托盘位置
    // 2 遍历waitingCarList, 返回距离最近的car
    public Car getNearestFreeCar(String carType, String srcNodeId){
        Position srcPosition = nodeManager.getNode(srcNodeId).getPosition();
        List<Car> waitingCarList = carManager.listWaitingCar(carType);
        if(waitingCarList == null){
            return null;
        }
        if(waitingCarList.isEmpty()){
            return null;
        }
        int min = Integer.MAX_VALUE;
        Car nearestFreeCar = new Car();
        for(Car car: waitingCarList){
            Position carPosition = car.getPosition();
            int distance = Math.abs(carPosition.x - srcPosition.x) + Math.abs(carPosition.y - srcPosition.y);
            if(distance < min){
                nearestFreeCar = car;
                min = distance;
            }
        }
        return nearestFreeCar;
    }

    @Override
    public Car getCar(String carId) {
        return carManager.getCar(carId);
    }

    public List<Car> listRunningCar(){
        return carManager.listRunningCar();
    }

    @Override
    public List<Car> listWaitingCar(String carType) {
        return carManager.listWaitingCar(carType);
    }
}
