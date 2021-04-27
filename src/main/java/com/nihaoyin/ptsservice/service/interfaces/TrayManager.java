package com.nihaoyin.ptsservice.service.interfaces;

import com.nihaoyin.ptsservice.bean.Tray;

public interface TrayManager {
    Tray getTray(String TrayId);
    void changeStatus(String trayId, String newStatus) throws Exception;
    void init();
}
