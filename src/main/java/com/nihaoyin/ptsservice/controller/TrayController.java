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
        try{
            if(status.equals("waiting")){
                return JsonUtil.success(trayService.listWaitingTrayId());
            }else if(status.equals("running")){
                return JsonUtil.success(trayService.listRunningTrayId());
            }else{
                return JsonUtil.failure("状态错误");
            }
        }catch(Exception e){
            return JsonUtil.failure(e.toString());
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
        switch (status){
            case "waiting":
                return JsonUtil.success(trayService.listWaitingTray());
            case "running":
                return JsonUtil.success(trayService.listRunningTray());
            default:
                return JsonUtil.failure("参数错误");
        }
    }

    @GetMapping("/count")
    public Object handleCountTray(@RequestParam("status") String status){
        switch (status){
            case "waiting":
                return JsonUtil.success(trayService.countWaitingTray());
            case "running":
                return JsonUtil.success(trayService.countRunningTray());
            default:
                return JsonUtil.failure("参数错误");
        }
    }
}
