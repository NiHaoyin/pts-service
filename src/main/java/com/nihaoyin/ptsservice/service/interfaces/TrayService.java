package com.nihaoyin.ptsservice.service.interfaces;

import com.nihaoyin.ptsservice.bean.Tray;

import java.util.List;

public interface TrayService {
    String getCurrentNode(String trayId) throws Exception;
    List<String> getAvailableNode(String trayId) throws Exception;
    List<String> listWaitingTrayId();
    List<String> listRunningTrayId();
    List<Tray> listWaitingTray();
    List<Tray> listRunningTray();
    int countWaitingTray();
    int countRunningTray();
    Tray getTray(String trayId);
}
