package com.nihaoyin.ptsservice.service.interfaces;

import com.nihaoyin.ptsservice.bean.Tray;

import java.util.List;

public interface TrayService {
    String getCurrentNode(String trayId) throws Exception;
    List<String> getAvailableNode(String trayId) throws Exception;
    List<String> listTrayId(String status);
    List<Tray> listTray(String status);
    int countTray(String status);
    Tray getTray(String trayId);
}
