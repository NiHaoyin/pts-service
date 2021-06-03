package com.nihaoyin.ptsservice.service.interfaces.manager;

import com.nihaoyin.ptsservice.bean.Node;
import com.nihaoyin.ptsservice.bean.Position;
import com.nihaoyin.ptsservice.bean.Trace;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface NodeManager {
    List<Trace> getPath(Position position, String src, String dst) throws Exception;
    List<Trace> getPath(Position position, String src1, String src2, String dst1, String dst2) throws IOException;
    boolean checkOccupied(String nodeId) throws Exception;
    String getTrayId(String nodeId) throws Exception;
    List<String> listUnoccupiedNodes(String trayType);
    List<String> listOccupiedNodes(String trayType);
    void placeTray(String nodeId, String TrayId) throws Exception;
    void removeTray(String nodeId) throws Exception;
    String getTrayType(String nodeId);
    Node getNode(String nodeId);
    Map<String, Node> getNodeMap();
}
