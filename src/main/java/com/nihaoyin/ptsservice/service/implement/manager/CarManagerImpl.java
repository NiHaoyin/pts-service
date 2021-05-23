package com.nihaoyin.ptsservice.service.implement.manager;

import com.nihaoyin.ptsservice.bean.Car;
import com.nihaoyin.ptsservice.bean.Node;
import com.nihaoyin.ptsservice.bean.Position;
import com.nihaoyin.ptsservice.service.interfaces.manager.CarManager;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class CarManagerImpl implements CarManager {
    private final static Logger logger = LoggerFactory.getLogger(CarManagerImpl.class);
    private final List<Car> waitingPBYSCList = new ArrayList<Car>();
    private final List<Car> waitingPBTCList1 = new ArrayList<Car>();
    private final List<Car> waitingPBTCList2 = new ArrayList<Car>();
    private final List<Car> waitingCCList = new ArrayList<Car>();
    private final List<Car> runningList = new ArrayList<Car>();

    // 集配初始化, 车辆开到最近的托盘集配点
    public void init(){

    }


    public List<Car> listRunningCar(){
        return runningList;
    }

    public List<Car> listWaitingCar(String carType){
        switch (carType) {
            case "PBYSC":
                return waitingPBYSCList;
            case "PBTC1":
                return waitingPBTCList1;
            case "PBTC2":
                return waitingPBTCList2;
            case "PBTC":
                List<Car> ret = new ArrayList<Car>();
                ret.addAll(waitingPBTCList1);
                ret.addAll(waitingPBTCList2);
                return ret;
            case "CC":
                return waitingCCList;
            case "all":
                List<Car> ret2 = new ArrayList<Car>();
                ret2.addAll(waitingPBTCList1);
                ret2.addAll(waitingPBTCList2);
                ret2.addAll(waitingCCList);
                ret2.addAll(waitingPBYSCList);
                return ret2;
            default:
                return null;
        }
    }
    public synchronized Car getCar(String carId){
        for(Car car : runningList){
            if(car.getCarId().equals(carId)){
                return car;
            }
        }
        for(Car car : waitingPBYSCList){
            if(car.getCarId().equals(carId)){
                return car;
            }
        }
        for(Car car : waitingPBTCList1){
            if(car.getCarId().equals(carId)){
                return car;
            }
        }
        for(Car car : waitingPBTCList2){
            if(car.getCarId().equals(carId)){
                return car;
            }
        }
        for(Car car : waitingCCList){
            if(car.getCarId().equals(carId)){
                return car;
            }
        }
        return null;
    }

    public synchronized void pushCar2WL(Car car){
        switch (car.getCarType()) {
            case "PBYSC":
                waitingPBYSCList.add(car);
                break;
            case "PBTC1":
                waitingPBTCList1.add(car);
                break;
            case "PBTC2":
                waitingPBTCList2.add(car);
                break;
            case "CC":
                waitingCCList.add(car);
                break;
        }
    }

    public synchronized void changeCarStatus(String carId, String newStatus)throws Exception{
        Car car = getCar(carId);
        if(car == null){
            throw new Exception("carId不存在");
        }
        if(newStatus.equals("running")){
            switch (car.getCarType()){
                case "PBYSC":
                    waitingPBYSCList.remove(car);
                    break;
                case "PBTC1":
                    waitingPBTCList1.remove(car);
                    break;
                case "PBTC2":
                    waitingPBTCList2.remove(car);
                    break;
                case "CC":
                    waitingCCList.remove(car);
                    break;
            }
            if(!runningList.contains(car)){
                runningList.add(car);
            }
            car.setStatus(newStatus);
        }else if(newStatus.equals("waiting")){
            runningList.remove(car);
            pushCar2WL(car);
            car.setStatus(newStatus);
        }else{
            throw new Exception("参数错误: "+newStatus+"不合法");
        }
    }

    public CarManagerImpl() throws IOException {
        XSSFWorkbook NodeXwb = new XSSFWorkbook("src/main/java/com/nihaoyin/ptsservice/config/car.xlsx");
        NodeXwb.close();
        XSSFSheet NodeSheet = NodeXwb.getSheetAt(0);
        XSSFRow row;
        String carId;
        String carType;
        double carX;
        double carY;
        int NLength= NodeSheet.getPhysicalNumberOfRows()-2;
        for (int i = 1; i <= NLength; i++)
        {
            row = NodeSheet.getRow(i);
            carId = row.getCell(0).getStringCellValue(); //获取运输车Id
            carType = row.getCell(1).getStringCellValue(); //运输车类型
            carX = row.getCell(4).getNumericCellValue();//获取集配点x坐标
            carY = row.getCell(5).getNumericCellValue();//获取集配点y坐标
            String driver = row.getCell(6).getStringCellValue();
            int speed = Integer.parseInt(row.getCell(7).getStringCellValue());
            int load = Integer.parseInt(row.getCell(8).getStringCellValue());
            int capacity = Integer.parseInt(row.getCell(9).getStringCellValue());
            Position carPosition= new Position((int)carX,(int)carY,0);

            Car car = new Car(carId, carType, speed, carPosition, load, "waiting", driver, capacity);
            pushCar2WL(car);
        }
    }

    public static void main(String[] args) throws IOException {
        CarManager carManager = new CarManagerImpl();
        List<Car> l1 = carManager.listWaitingCar("PBYSC");
        for(Car c: l1){
            System.out.println(c.toString());
        }
        l1 = carManager.listRunningCar();
        for(Car c: l1){
            System.out.println(c.toString());
        }
    }

}
