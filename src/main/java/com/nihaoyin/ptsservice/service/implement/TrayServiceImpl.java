package com.nihaoyin.ptsservice.service.implement;

import com.nihaoyin.ptsservice.bean.Tray;
import com.nihaoyin.ptsservice.service.implement.manager.ResourceManagerFactory;
import com.nihaoyin.ptsservice.service.interfaces.TrayService;
import com.nihaoyin.ptsservice.service.interfaces.manager.ResourceManager;

import java.util.ArrayList;
import java.util.List;

public class TrayServiceImpl implements TrayService {
    private final ResourceManager resourceManager = ResourceManagerFactory.getResourceManager();

    @Override
    public String getCurrentNode(String trayId) throws Exception {
        return resourceManager.getNodeId(trayId);
    }

    @Override
    public List<String> getAvailableNode(String trayId) throws Exception {
        return resourceManager.listAvailableNode(trayId);
    }

    @Override
    public List<String> listWaitingTrayId() {
        return resourceManager.listTrayId("waiting");
    }

    @Override
    public List<String> listRunningTrayId() {
        return resourceManager.listTrayId("running");
    }

    @Override
    public List<Tray> listWaitingTray() {
        List<Tray> ret = new ArrayList<Tray>();
        List<String> trayIdList = listWaitingTrayId();
        for(String s: trayIdList){
            ret.add(resourceManager.getTray(s));
        }
        return ret;
    }

    @Override
    public List<Tray> listRunningTray() {
        List<Tray> ret = new ArrayList<Tray>();
        List<String> trayIdList = listRunningTrayId();
        for(String s: trayIdList){
            ret.add(resourceManager.getTray(s));
        }
        return ret;
    }

    @Override
    public int countWaitingTray() {
        List<String> trayIdList = listWaitingTrayId();
        return trayIdList.size();
    }

    @Override
    public int countRunningTray() {
        List<String> trayIdList = listRunningTrayId();
        return trayIdList.size();
    }

    @Override
    public Tray getTray(String trayId) {
        return resourceManager.getTray(trayId);
    }


}
