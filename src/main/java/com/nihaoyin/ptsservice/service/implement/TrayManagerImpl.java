package com.nihaoyin.ptsservice.service.implement;

import com.nihaoyin.ptsservice.bean.Tray;
import com.nihaoyin.ptsservice.service.interfaces.TrayManager;

import java.util.ArrayList;
import java.util.List;

public class TrayManagerImpl implements TrayManager {
    private List<Tray> waitingList = new ArrayList<Tray>();
    private List<Tray> runningList = new ArrayList<Tray>();

    public Tray getTray(String trayId){
        for(Tray tray : waitingList){
            if(tray.getTrayId().equals(trayId)){
                return tray;
            }
        }
        for(Tray tray : runningList){
            if(tray.getTrayId().equals(trayId)){
                return tray;
            }
        }
        return null;
    }

    public void changeStatus(String trayId, String newStatus) throws Exception{
        Tray tray = getTray(trayId);
        if(tray.getStatus().equals(newStatus)){
            throw new Exception("参数错误，该托盘已经处于 "+newStatus+" 状态");
        }
        if(newStatus.equals("running")){
            waitingList.remove(tray);
            runningList.add(tray);
            tray.setStatus("running");
        }else if(newStatus.equals("waiting")){
            runningList.remove(tray);
            waitingList.add(tray);
            tray.setStatus("waiting");
        }else{
            throw new Exception("参数错误: "+newStatus+"不合法");
        }
    }

    // 集配初始化
    public void init(){

    }


}
