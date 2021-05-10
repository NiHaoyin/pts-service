package com.nihaoyin.ptsservice.service.implement;

import com.nihaoyin.ptsservice.bean.Node;
import com.nihaoyin.ptsservice.bean.Order;
import com.nihaoyin.ptsservice.dao.OrderDao;
import com.nihaoyin.ptsservice.service.implement.manager.ResourceManagerFactory;
import com.nihaoyin.ptsservice.service.interfaces.OrderService;
import com.nihaoyin.ptsservice.service.interfaces.manager.ResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService, InitializingBean {
    private final ResourceManager resourceManager = ResourceManagerFactory.getResourceManager();
    private int nextOrderId;
    @Autowired
    OrderDao orderDao;
    private final static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    public int countWaitingOrder() {
        Queue<Order> waitingList = resourceManager.listWaitingOrder();
        return waitingList.size();
    }

    public int countRunningOrder() {
        List<Order> runningList = resourceManager.listRunningOrder();
        return runningList.size();
    }

    public int countFinishedOrder(){
        return 0;
    }

    public Order getOrder(int orderId){
        Order order = resourceManager.getOrder(orderId);
        if(order == null){
            order = orderDao.getOrder(orderId);
        }
        return order;
    }

    public Queue<Order> listWaitingOrder() {
        return resourceManager.listWaitingOrder();
    }

    public List<Order> listRunningOrder() {
        return resourceManager.listRunningOrder();
    }

    // 从数据库中拿
    public List<Order> listFinishedOrder(int base, int offset) {
        return orderDao.listOrder(true, base, offset);
    }

    public synchronized void placeOrder(Order newOrder) throws Exception {
        if(resourceManager.checkParam(newOrder)){
            newOrder.setCreated(new Date().toString());
            newOrder.setOrderId(nextOrderId);
            nextOrderId++;
            resourceManager.placeOrder(newOrder);
            orderDao.addOrder(newOrder);
            //logger.info("成功下单 {}", newOrder.toString());
        }else{
            //logger.info("订单参数错误 {}", newOrder.toString());
            throw new Exception("订单参数不正确");
        }
    }

    public void deleteOrder(int orderId) throws Exception {
        resourceManager.deleteOrder(orderId);
        orderDao.deleteOrder(orderId);
    }

    public synchronized void init() throws Exception {
        nextOrderId = orderDao.getNextOrderId();
        Map<String, Node> nodeMap = resourceManager.getNodeMap();
        Random r = new Random(1);
        int i = 0;
        for (Map.Entry<String, Node> entry : nodeMap.entrySet()) {
            Node node = entry.getValue();
            i++;
            if(i > 20){
                break;
            }
            if (node.isOccupied()) {
                String src = node.getNodeId();
                String trayId = node.getTrayId();
                for (Map.Entry<String, Node> x : nodeMap.entrySet()) {
                    Node dstNode = x.getValue();
                    if (!dstNode.isOccupied()
                        && (dstNode.getTrayType().equals(node.getTrayType()))
                        && !dstNode.getNodeId().equals(src)
                        && resourceManager.getTray(trayId).getStatus().equals("waiting")) {
                        String dst = dstNode.getNodeId();
                        Order order = new Order(src, dst, trayId, nextOrderId, r.nextInt(3)+1, 1);
                        placeOrder(order);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}
