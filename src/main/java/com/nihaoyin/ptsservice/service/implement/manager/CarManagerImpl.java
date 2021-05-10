package com.nihaoyin.ptsservice.service.implement.manager;

import com.nihaoyin.ptsservice.bean.Car;
import com.nihaoyin.ptsservice.service.interfaces.manager.CarManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CarManagerImpl implements CarManager {
    private final static Logger logger = LoggerFactory.getLogger(CarManagerImpl.class);
    private final Queue<Car> waitingList1 = new PriorityQueue<Car>(
            new Comparator<Car>(){
                // 载重小的在队首
                @Override
                public int compare(Car c1, Car c2){
                    if (c1.getLoad() - c2.getLoad() > 0){
                        return 1;
                    }else if(c1.getLoad() - c2.getLoad() == 0){
                        return 0;
                    }
                    return -1;
                }
            }
    );
    private final Queue<Car> waitingList2 = new PriorityQueue<Car>();
    private final Queue<Car> waitingList3 = new PriorityQueue<Car>();
    private final List<Car> runningList = new ArrayList<Car>();

    // 集配初始化
    public void init(){

    }
    public List<Car> listRunningCar(){
        return runningList;
    }

    public synchronized Car getCar(String carId){
        for(Car car : runningList){
            if(car.getCarId().equals(carId)){
                return car;
            }
        }
        for(Car car : waitingList1){
            if(car.getCarId().equals(carId)){
                return car;
            }
        }
        for(Car car : waitingList2){
            if(car.getCarId().equals(carId)){
                return car;
            }
        }
        for(Car car : waitingList3){
            if(car.getCarId().equals(carId)){
                return car;
            }
        }
        return null;
    }

    public synchronized void pushCar(Car car, String targetList){
        switch (targetList) {
            case "waitingList1":
                waitingList1.add(car);
                break;
            case "waitingList2":
                waitingList2.add(car);
                break;
            case "waitingList3":
                waitingList3.add(car);
                break;
            case "runningList":
                runningList.add(car);
                break;
        }
    }

    public synchronized void changeStatus(String carId, String newStatus)throws Exception{
        Car car = getCar(carId);
        if(car.getStatus().equals(newStatus)){
            throw new Exception("参数错误，该车辆已经处于 "+newStatus+" 状态");
        }
        if(newStatus.equals("running")){
            waitingList1.remove(car);
            waitingList2.remove(car);
            waitingList3.remove(car);
            runningList.add(car);
            car.setStatus(newStatus);
        }else if(newStatus.equals("waiting")){
            runningList.remove(car);
            switch (car.getCarType()){
                case "PBYS":
                    waitingList1.add(car);
                    break;

            }
            car.setStatus(newStatus);
        }else{
            throw new Exception("参数错误: "+newStatus+"不合法");
        }
    }

    public static void main(String[] args) {
        CarManager carManager = new CarManagerImpl();

    }

}
