package com.nihaoyin.ptsservice.service.implement.router;


import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;

public class Dijkstra {
    /*
     * 参数Origin:为路径起点
     * 参数Destination:为路径终点
     * 参数Matrix:为道路图模型的邻接矩阵，其中值为-1的元素表示顶点间不直接相连
     * 参数Distance:为起点到图上各点的最短距离
     * 参数Path:为起点到图上各点的最短路径0
     * 参数singlePath:为起点到终点的最短路径
     * 构造函数Dijkstra:输入参数Origin、Destination和Matrix
     * 函数getShortestPaths:计算起点到图上各点的最短距离、对应路径和起点到终点的最短路径，分别存入参数Distance、Path和singlePath
     * 函数getDistance:输出Distance
     * 函数getPath:输出Path
     * 函数getSinglePath:输出singlePath
     */
    private int Origin;
    private int Destination;
    private int[][] Matrix;
    private int[] Distance;
    private int[][] Path;
    private int[] SinglePath;

    public Dijkstra(int startPoint, int endPoint) throws IOException{
        Origin = startPoint;
        Destination = endPoint;
        Matrix = readDMatrix();
        Distance=new int[Matrix.length];
        Path=new int[Matrix.length][Matrix.length];
        for(int i = 0;i < Path.length;i++){
            for(int j = 0;j < Path.length;j++){
                Path[i][j]=-1;
            }
        }
        int[] tempDistance = new int[Matrix.length];   //用于存放起点到其它顶点的最短距离
        int[] tempPath = new int[Matrix.length];   //用于存放起点到各点最短路径的上一点标号
        for (int i = 0; i < tempPath.length; i++) {
            tempPath[i] = -1;
        }
        tempPath[Origin] = Origin;
        boolean[] used = new boolean[Matrix.length];  //用于判断顶点是否被遍历
        for (int i = 0; i < tempDistance.length; i++) {
            tempDistance[i] = Matrix[Origin][i];
            used[i] = false;
        }
        used[Origin] = true;  //表示顶点Origin已被遍历
        int Sum = 1;   //用于存放被遍历的点的个数
        int lastPoint = -1;
        int newPoint = -1;
        //Dijkstra算法
        while (Sum < Matrix.length) {
            int Min = Integer.MAX_VALUE;
            for (int i = 0; i < Matrix.length; i++) {
                if (used[i] == true && tempDistance[i]!=-1) {
                    for (int j = 0; j < Matrix.length; j++) {
                        if(used[j] == false && Matrix[i][j] != -1){
                            int Plus = tempDistance[i] + Matrix[i][j];
                            if (Plus < Min) {
                                Min = Plus;
                                lastPoint = i;
                                newPoint = j;
                            }
                        }
                    }
                }
            }
            tempDistance[newPoint] = Min;
            used[newPoint] = true;
            tempPath[newPoint] = lastPoint;
            Sum++;
        }
        Distance = tempDistance;
        //获取路由矩阵
        for (int i = 0; i < Path.length; i++) {
            Path[i][Path.length - 1] = i;
            if (i != Origin) {
                Path[i][Matrix.length - 2] = tempPath[i];
                for (int j = 0; j < Matrix.length - 2; j++) {
                    if (Path[i][Matrix.length - (j + 2)] == Origin) {
                        break;
                    }
                    Path[i][Matrix.length - (j + 3)] = tempPath[Path[i][Matrix.length - (j + 2)]];
                }
            }
        }
        //获取到指定终点的路径
        for (int i=0;i<Path.length;i++){
            if (Path[Destination][i]==Origin){
                SinglePath=new int[Path.length-i];
                for (int j=i;j<Path.length;j++){
                    SinglePath[j-i]=Path[Destination][j];
                }
                break;
            }
        }
    }

    public void showDistance(){
        System.out.println("起始点到图中所有顶点之间的最短距离为：");
        for(int i = 0;i < Distance.length;i++)
            System.out.print(Distance[i]+" ");
    }

    public void showPath(){
        System.out.println("起点到图中所有顶点之间的最短路径为：");
        for(int i = 0;i < Path.length;i++){
            for(int j = 0;j < Path.length-1;j++){
            System.out.print(Path[i][j]+" ");
            }
            System.out.println(Path[i][Path.length-1]);
        }
    }

    public void showSinglePath(){
        System.out.println("起点"+Origin+"到终点"+Destination+"之间的最短路径为：");
        for(int i = 0;i < SinglePath.length;i++)
            System.out.print(SinglePath[i]+" ");
    }

    public int[] getSinglePath(){
        return SinglePath;
    }
    public int getDistance() { return Distance[Destination]; }

    public static int[][] readDMatrix() throws IOException
    {
        XSSFWorkbook DMatrixXwb = new XSSFWorkbook("src/main/java/com/nihaoyin/ptsservice/config/dMatrix.xlsx");
        DMatrixXwb.close();
        XSSFSheet DMatrixSheet = DMatrixXwb.getSheetAt(0);
        XSSFRow DMatrixRow;
        double DMatrixCell;
        int DLength= DMatrixSheet.getPhysicalNumberOfRows()-1;
        int[][] DMatrix= new int[DLength][DLength];
        for (int i = 0; i < DLength; i++)
        {
            DMatrixRow = DMatrixSheet.getRow(i+1);
            for (int j = 0; j < DLength; j++)
            {
                DMatrixCell = DMatrixRow.getCell(j+1).getNumericCellValue();
                DMatrix[i][j]=(int)DMatrixCell;
            }
        }
        return DMatrix;
    }

    public static void main(String[] args) throws IOException{
        Dijkstra test = new Dijkstra(17,36);
        test.showSinglePath();
    }
}
