package com.nihaoyin.ptsservice.controller;

import com.nihaoyin.ptsservice.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@RestController
@CrossOrigin
@ServerEndpoint("/simulation")
public class SimulationController {
    private final static Logger logger = LoggerFactory.getLogger(SimulationController.class);
    private boolean simulationSwitch = false;
    MyThread mt1 = new MyThread("线程A ");
    Thread t1 = new Thread(mt1) ;

    @OnOpen
    public Object startSimulating(){
        logger.info("开始模拟");
        simulationSwitch = true;
        t1.start();
        return JsonUtil.success();
    }

    @OnClose
    public Object finishSimulating(){
        logger.info("结束模拟");
        simulationSwitch = false;

        return JsonUtil.success();
    }

    static class MyThread implements Runnable{
        private String name ;       // 表示线程的名称
        public MyThread(String name){
        this.name = name;
        } ;      // 通过构造方法配置name属性}
      public void run(){  // 覆写run()方法，作为线程 的操作主体
          for(int i=0;i<10;i++){
              System.out.println(name + "运行，i = " + i) ;
          }
//          System.out.println("thread finished");
          try{
              Thread.sleep(10000);
          }catch(Exception e){
              e.printStackTrace();
          }
          System.out.println("thread finished");

      }

    }
}
