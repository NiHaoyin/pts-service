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
    public List<String> listTrayId(String status) {
        return resourceManager.listTrayId(status);
    }

    @Override
    public List<Tray> listTray(String status) {
        List<Tray> ret = new ArrayList<Tray>();
        List<String> trayIdList = listTrayId(status);
        for(String s: trayIdList){
            ret.add(resourceManager.getTray(s));
        }
        return ret;
    }

    @Override
    public int countTray(String status) {
        List<String> trayIdList = listTrayId(status);
        return trayIdList.size();
    }

    @Override
    public Tray getTray(String trayId) {
        return resourceManager.getTray(trayId);
    }


}
