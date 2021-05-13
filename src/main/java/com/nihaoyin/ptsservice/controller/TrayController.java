package com.nihaoyin.ptsservice.controller;

import com.nihaoyin.ptsservice.bean.Tray;
import com.nihaoyin.ptsservice.service.implement.TrayServiceImpl;
import com.nihaoyin.ptsservice.service.interfaces.TrayService;
import com.nihaoyin.ptsservice.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/tray")
public class TrayController {
    private final static Logger logger = LoggerFactory.getLogger(TrayController.class);
    private final TrayService trayService = new TrayServiceImpl();

    @GetMapping("/getcurrentnode")
    public Object handleGetCurrentNode(@RequestParam("trayId") String trayId){
        logger.info("新请求 {}", trayId);
        try{
            String nodeId = trayService.getCurrentNode(trayId);
            return JsonUtil.success(nodeId);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.failure(e.toString());
        }
    }

    @GetMapping("/getavailablenode")
    public Object handleGetAvailableNode(@RequestParam("trayId") String trayId){
        try{
            List<String> ret = trayService.getAvailableNode(trayId);
            return JsonUtil.success(ret);
        } catch (Exception e) {
            return JsonUtil.failure(e.toString());
        }
    }

    @GetMapping("/listid")
    public Object handleListTrayId(@RequestParam("status") String status){
        if(status.equals("running") || status.equals("noOrder")
           || status.equals("noorder") || status.equals("waiting")){
            return JsonUtil.success(trayService.listTrayId(status));
        }else{
            return JsonUtil.failure("参数错误");
        }
    }

    @GetMapping("/get")
    public Object handleGetTray(@RequestParam("trayid") String trayId){
        Tray t = trayService.getTray(trayId);
        if(t == null){
            return JsonUtil.failure("trayId不存在");
        }else{
            return JsonUtil.success(t);
        }
    }

    @GetMapping("/list")
    public Object handleListTray(@RequestParam("status") String status){
        if(status.equals("running") || status.equals("noOrder")
                || status.equals("noorder") || status.equals("waiting")){
            return JsonUtil.success(trayService.listTray(status));
        }else{
            return JsonUtil.failure("参数错误");
        }
    }

    @GetMapping("/count")
    public Object handleCountTray(@RequestParam("status") String status){
        if(status.equals("running") || status.equals("noOrder")
                || status.equals("noorder") || status.equals("waiting")){
            return JsonUtil.success(trayService.countTray(status));
        }else{
            return JsonUtil.failure("参数错误");
        }
    }
}
