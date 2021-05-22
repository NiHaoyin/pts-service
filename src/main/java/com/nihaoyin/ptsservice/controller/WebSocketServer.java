package com.nihaoyin.ptsservice.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nihaoyin.ptsservice.bean.*;
import com.nihaoyin.ptsservice.dao.OrderDao;
import com.nihaoyin.ptsservice.service.implement.manager.ResourceManagerFactory;
import com.nihaoyin.ptsservice.service.implement.router.PathPlanning;
import com.nihaoyin.ptsservice.service.interfaces.OrderService;
import com.nihaoyin.ptsservice.service.interfaces.manager.ResourceManager;
import com.nihaoyin.ptsservice.util.JsonUtil;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.apache.tomcat.util.buf.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author zhengkai.blog.csdn.net
 */
@ServerEndpoint("/imserver")
@Service
@Controller
@Component
@RestController
@CrossOrigin
public class WebSocketServer {
    private final static Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    private static boolean simulationSwitch = false;
    @Autowired
    OrderService orderService;
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session = null;
    private final ResourceManager resourceManager = ResourceManagerFactory.getResourceManager();
    private SimulationThread simulator = new SimulationThread("simulator");

    class SimulationThread extends Thread {
        private String name;       // 表示线程的名称
        public SimulationThread(String name) {
            this.name = name;      // 通过构造方法配置name属性
        }
        public void run() {
            try {
                startSimulation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void startSimulation() throws Exception {
        List<Order> orders = new ArrayList<Order>();
        while (simulationSwitch) {
            try {
                orders = resourceManager.scheduleOrder();
//                System.out.println(orders.get(0).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (orders.isEmpty()) {
                Thread.sleep(5000);
            }else{
                if (orders.size() == 1) {
                    Order order = orders.get(0);
                    Position carPosition = resourceManager.getCar(order.getCarId()).getPosition();
                    try {
                        List<Trace> trace = getTrace(carPosition, order.getSrc(), order.getDst());
                        Map<String, Object> res = new HashMap<String, Object>();
                        res.put("command", "navigate");
                        res.put("data", trace);
                        sendMessage(JsonUtil.success(res));
                    }catch (Exception e) {
                        e.printStackTrace();
                        Thread.sleep(5000);
                    }
                } else if (orders.size() == 2) {
                    Order order1 = orders.get(0);
                    Order order2 = orders.get(1);
                    Position carPosition = resourceManager.getCar(order1.getCarId()).getPosition();
                    try {
                        List<Trace> trace = getTrace(carPosition, order1.getSrc(), order2.getSrc(), order1.getDst(), order2.getDst());
                        sendMessage(trace.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public List<Trace> getTrace(Position carPosition, String srcNodeId, String dstNodeId) throws IOException {
        PathPlanning pathPlanning = new PathPlanning(carPosition, srcNodeId, "", dstNodeId, "");
        return pathPlanning.getTraceList();
    }

    public List<Trace> getTrace(Position carPosition, String srcNodeId1, String srcNodeId2, String dstNodeId1, String dstNodeId2) throws IOException {
        PathPlanning pathPlanning = new PathPlanning(carPosition, srcNodeId1, srcNodeId2, dstNodeId1, dstNodeId2);
        return pathPlanning.getTraceList();
    }

    @OnOpen
    public void onOpen(Session session) {
        logger.info("用户连接 {}", session);
        if (this.session == null) {
            this.session = session;
            System.out.println(this.session);
        } else {
            try {
                sendMessage("连接失败");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void onClose() {
        this.session = null;
        simulationSwitch = false;
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
//        logger.info("报文: " + message);
        //可以群发消息
        //消息保存到数据库、redis
        try {
            //解析发送的报文
            JSONObject jsonObject = JSON.parseObject(message);
            System.out.println(jsonObject);
            String command = jsonObject.getString("command");
            switch (command) {
                case "startSimulation":
                    simulationSwitch = true;
                    simulator.start();
                    break;
                case "stopSimulation":
                    simulationSwitch = false;
                    simulator.join();
                    break;
                case "initNode":
                    Map<String, Node> nodeMap = resourceManager.getNodeMap();
//                    List<Car> runningCar = resourceManager.listRunningCar();
//                    List<Car> waitingCar = resourceManager.listWaitingCar("all");
//                    runningCar.addAll(waitingCar);
                    Map<String, Object> res = new HashMap<String, Object>();
                    res.put("command", "initNode");
                    res.put("data", nodeMap);
                    sendMessage(JsonUtil.success(res));
                    break;
                case "initCar":
                    List<Car> runningCar = resourceManager.listRunningCar();
                    List<Car> waitingCar = resourceManager.listWaitingCar("all");
                    runningCar.addAll(waitingCar);
                    Map<String, Object> res2 = new HashMap<String, Object>();
                    res2.put("command", "initCar");
                    res2.put("data", runningCar);
                    sendMessage(JsonUtil.success(res2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("错误: " + error.getMessage());
        sendMessage(JsonUtil.failure(error.toString()));
        error.printStackTrace();
    }

    public void sendMessage(String message){
        logger.info("发送消息: {}", message);
        try{
            this.session.getBasicRemote().sendText(message);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
