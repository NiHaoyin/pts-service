package com.nihaoyin.ptsservice.service.interfaces.manager;

import com.nihaoyin.ptsservice.bean.Car;
import com.nihaoyin.ptsservice.bean.Position;

import java.util.List;

public interface CarManager {
    void init(); // 集配初始化
    List<Car> listRunningCar();
    List<Car> listWaitingCar(String carType);
    Car getCar(String carId);
    void pushCar2WL(Car car);
    void changeCarStatus(String carId, String newStatus) throws Exception;
}
