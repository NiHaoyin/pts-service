package com.nihaoyin.ptsservice.service.interfaces.manager;

import com.nihaoyin.ptsservice.bean.Tray;

import java.io.IOException;
import java.util.List;

public interface TrayManager {
    Tray getTray(String trayId);
    void changeStatus(String trayId, String newStatus) throws Exception;
    // void init();
    List<Tray> listTray(String status);
    void moveTray(String trayId, String newNodeId); // move tray to a new node
    void reset() throws IOException;
}
