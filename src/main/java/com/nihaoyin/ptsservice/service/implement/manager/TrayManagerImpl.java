package com.nihaoyin.ptsservice.service.implement.manager;

import com.nihaoyin.ptsservice.bean.Node;
import com.nihaoyin.ptsservice.bean.Position;
import com.nihaoyin.ptsservice.bean.Tray;
import com.nihaoyin.ptsservice.service.interfaces.manager.TrayManager;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrayManagerImpl implements TrayManager {
    Map<String, Tray> trayMap = new HashMap<String, Tray>();

    @Override
    public Tray getTray(String trayId) {
        return trayMap.get(trayId);
    }

    @Override
    public void changeStatus(String trayId, String newStatus) throws Exception {
        Tray tray = trayMap.get(trayId);
        if(tray == null){
            throw new Exception("该trayId不存在");
        }
        if(tray.getStatus().equals(newStatus)){
            throw new Exception("状态错误");
        }
        tray.setStatus(newStatus);
    }

    public void moveTray(String trayId, String nodeId){
        Tray tray = trayMap.get(trayId);
        tray.setNodeId(nodeId);
    }

    public TrayManagerImpl() throws IOException {
        XSSFWorkbook NodeXwb = new XSSFWorkbook("src/main/java/com/nihaoyin/ptsservice/config/node_1.xlsx");
        NodeXwb.close();
        XSSFSheet NodeSheet = NodeXwb.getSheetAt(0);
        XSSFRow NodeRow;
        String nodeId;
        String trayType;
        String trayId;
        int NLength= NodeSheet.getPhysicalNumberOfRows()-2;
        for (int i = 1; i <= NLength; i++)
        {
            NodeRow = NodeSheet.getRow(i);
            nodeId = NodeRow.getCell(0).getStringCellValue();///获取集配点编号
            trayType = NodeRow.getCell(1).getStringCellValue();//获取集配点对应托盘类型
            try{
                trayId = NodeRow.getCell(10).getStringCellValue();//获取集配点存放的托盘Id
            }catch (Exception e){
                continue;
            }
            Tray tray = new Tray(trayType, trayId, "firstCarType", "secondCarType", nodeId);
            tray.setStatus("waiting");
            trayMap.put(trayId, tray);
        }
    }

    @Override
    public List<Tray> listRunningTray() {
        List<Tray> ret = new ArrayList<Tray>();
        for (Map.Entry<String, Tray> entry : trayMap.entrySet()) {
            Tray tray = entry.getValue();
            if(tray.getStatus().equals("running")){
                ret.add(tray);
            }
        }
        return ret;
    }

    @Override
    public List<Tray> listWaitingTray() {
        List<Tray> ret = new ArrayList<Tray>();
        for (Map.Entry<String, Tray> entry : trayMap.entrySet()) {
            Tray tray = entry.getValue();
            if(tray.getStatus().equals("waiting")){
                ret.add(tray);
            }
        }
        return ret;
    }

    public static void main(String[] args) throws IOException {
        TrayManager trayManager = new TrayManagerImpl();
        List<Tray> waitingTray = trayManager.listWaitingTray();
        List<Tray> runningTray = trayManager.listRunningTray();
        for(Tray t: waitingTray){
            System.out.println(t);
        }
    }
}
