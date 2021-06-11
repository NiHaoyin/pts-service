package com.nihaoyin.ptsservice.service.implement.manager;

import com.nihaoyin.ptsservice.bean.Node;
import com.nihaoyin.ptsservice.bean.Position;
import com.nihaoyin.ptsservice.bean.Trace;
import com.nihaoyin.ptsservice.service.implement.router.PathPlanning;
import com.nihaoyin.ptsservice.service.interfaces.manager.NodeManager;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeManagerImpl implements NodeManager {
    private final static Logger logger = LoggerFactory.getLogger(NodeManagerImpl.class);
    Map<String, Node> nodeMap = new HashMap<String, Node>();

    @Override
    public List<Trace> getPath(Position position, String src, String dst) throws Exception{
        PathPlanning pathPlanning = new PathPlanning(position, src, "", dst, "");
        return pathPlanning.getTraceList();
    }

    @Override
    public List<Trace> getPath(Position position, String src1, String src2, String dst1, String dst2) throws IOException {
        PathPlanning pathPlanning = new PathPlanning(position, src1, src2, dst1, dst2);
        return pathPlanning.getTraceList();
    }

    public boolean checkOccupied(String nodeId) throws Exception{
        Node node = nodeMap.get(nodeId);
        if(node == null){
            throw new Exception("该nodeId不存在");
        }
        return node.isOccupied();
    }

    public String getTrayId(String nodeId)throws Exception{
        Node node = nodeMap.get(nodeId);
        if(node == null){
            throw new Exception("该nodeId不存在");
        }
        return node.getTrayId();
    }

    public List<String> listUnoccupiedNodes(String trayType){
        List<String> ret = new ArrayList<String>();
        for (Map.Entry<String, Node> entry : nodeMap.entrySet()) {
            if(entry.getValue().getTrayType().equals(trayType) && !entry.getValue().isOccupied()){
                ret.add(entry.getKey());
            }
        }
        return ret;
    }

    public List<String> listOccupiedNodes(String trayType){
        List<String> ret = new ArrayList<String>();
        for (Map.Entry<String, Node> entry : nodeMap.entrySet()) {
            if(entry.getValue().getTrayType().equals(trayType) && entry.getValue().isOccupied()){
                ret.add(entry.getKey());
            }
        }
        return ret;
    }

    @Override
    public synchronized void placeTray(String nodeId, String trayId) throws Exception {
        Node node = nodeMap.get(nodeId);
        if(node == null){
            throw new Exception("该nodeId不存在");
        }
        node.setTrayId(trayId);
        node.setOccupied(true);
    }

    public synchronized void removeTray(String nodeId) throws Exception{
        Node node = nodeMap.get(nodeId);
        if(node == null){
            throw new Exception("该nodeId不存在");
        }
        if(!node.isOccupied()){
            throw new Exception("该node没有存放托盘");
        }
        node.setTrayId("");
        node.setOccupied(false);
    }

    @Override
    public String getTrayType(String nodeId) {
        Node node = nodeMap.get(nodeId);
        return node.getTrayType();
    }

    @Override
    public Node getNode(String nodeId) {
        return nodeMap.get(nodeId);
    }

    public Map<String, Node> getNodeMap(){
        return nodeMap;
    }

    public NodeManagerImpl() throws IOException {
        XSSFWorkbook NodeXwb = new XSSFWorkbook("src/main/java/com/nihaoyin/ptsservice/config/node_1.xlsx");
        NodeXwb.close();
        XSSFSheet NodeSheet = NodeXwb.getSheetAt(0);
        XSSFRow NodeRow;
        String NodeId;
        String NodeType;
        double NodeX;
        double NodeY;
        String NodeState;
        String TrayId;
        boolean NodeIsOccupied;
        int NodeVertex1;
        int NodeVertex2;
        String NodeMotionType;
        int NLength= NodeSheet.getPhysicalNumberOfRows()-2;
        for (int i = 1; i <= NLength; i++)
        {
            NodeRow = NodeSheet.getRow(i);
            NodeId = NodeRow.getCell(0).getStringCellValue();///获取集配点编号
            NodeType = NodeRow.getCell(1).getStringCellValue();//获取集配点对应托盘类型
            NodeX = NodeRow.getCell(4).getNumericCellValue();//获取集配点x坐标
            NodeY = NodeRow.getCell(5).getNumericCellValue();//获取集配点y坐标
            try{
                TrayId = NodeRow.getCell(10).getStringCellValue();//获取集配点存放的托盘Id
            }catch (Exception e){
                TrayId = "";
            }

            Position NodePosition= new Position((int)NodeX,(int)NodeY,0);
            NodeState= NodeRow.getCell(6).getStringCellValue();//获取集配点占用情况
            if (NodeState.equals("True")){
                NodeIsOccupied=true;
            } else{
                NodeIsOccupied=false;
            }
            NodeVertex1=Integer.parseInt(NodeRow.getCell(7).getStringCellValue());//获取集配点对应节点1
            NodeVertex2=Integer.parseInt(NodeRow.getCell(8).getStringCellValue());//获取集配点对应节点2
            NodeMotionType=NodeRow.getCell(9).getStringCellValue();//获取集配点移动方式
            Node node = new Node(NodeId, NodePosition,NodeIsOccupied, TrayId, NodeType,NodeVertex1,NodeVertex2,NodeMotionType);;
//            if(NodeIsOccupied){
//                //node= new Node(NodeId, NodePosition,NodeIsOccupied, "trayNum", NodeType,NodeVertex1,NodeVertex2,NodeMotionType);
//                node.setTrayId("trayId");
//            }

            nodeMap.put(node.getNodeId(), node);
        }
    }

    public void reset() throws IOException {
        nodeMap.clear();
        XSSFWorkbook NodeXwb = new XSSFWorkbook("src/main/java/com/nihaoyin/ptsservice/config/node_1.xlsx");
        NodeXwb.close();
        XSSFSheet NodeSheet = NodeXwb.getSheetAt(0);
        XSSFRow NodeRow;
        String NodeId;
        String NodeType;
        double NodeX;
        double NodeY;
        String NodeState;
        String TrayId;
        boolean NodeIsOccupied;
        int NodeVertex1;
        int NodeVertex2;
        String NodeMotionType;
        int NLength= NodeSheet.getPhysicalNumberOfRows()-2;
        for (int i = 1; i <= NLength; i++)
        {
            NodeRow = NodeSheet.getRow(i);
            NodeId = NodeRow.getCell(0).getStringCellValue();///获取集配点编号
            NodeType = NodeRow.getCell(1).getStringCellValue();//获取集配点对应托盘类型
            NodeX = NodeRow.getCell(4).getNumericCellValue();//获取集配点x坐标
            NodeY = NodeRow.getCell(5).getNumericCellValue();//获取集配点y坐标
            try{
                TrayId = NodeRow.getCell(10).getStringCellValue();//获取集配点存放的托盘Id
            }catch (Exception e){
                TrayId = "";
            }

            Position NodePosition= new Position((int)NodeX,(int)NodeY,0);
            NodeState= NodeRow.getCell(6).getStringCellValue();//获取集配点占用情况
            if (NodeState.equals("True")){
                NodeIsOccupied=true;
            } else{
                NodeIsOccupied=false;
            }
            NodeVertex1=Integer.parseInt(NodeRow.getCell(7).getStringCellValue());//获取集配点对应节点1
            NodeVertex2=Integer.parseInt(NodeRow.getCell(8).getStringCellValue());//获取集配点对应节点2
            NodeMotionType=NodeRow.getCell(9).getStringCellValue();//获取集配点移动方式
            Node node = new Node(NodeId, NodePosition,NodeIsOccupied, TrayId, NodeType,NodeVertex1,NodeVertex2,NodeMotionType);;
//            if(NodeIsOccupied){
//                //node= new Node(NodeId, NodePosition,NodeIsOccupied, "trayNum", NodeType,NodeVertex1,NodeVertex2,NodeMotionType);
//                node.setTrayId("trayId");
//            }
            nodeMap.put(node.getNodeId(), node);
        }
    }
    public static void main(String[] args) throws Exception {
        NodeManager nodeManager = new NodeManagerImpl();
        for(String s: nodeManager.listOccupiedNodes("ZD")){
            System.out.println(s);
            System.out.println(nodeManager.getTrayId(s));
        }
        for(String s: nodeManager.listUnoccupiedNodes("ZD")){
            System.out.println(s);
            System.out.println(nodeManager.getTrayId(s));
        }
        try{
            nodeManager.removeTray("ZD");
        }catch(Exception e){
            e.printStackTrace();
        }

    }


}
