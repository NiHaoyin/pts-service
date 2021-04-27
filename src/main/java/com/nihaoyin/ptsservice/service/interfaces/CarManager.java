package com.nihaoyin.ptsservice.service.interfaces;

import com.nihaoyin.ptsservice.bean.Car;
import com.nihaoyin.ptsservice.bean.Position;

import java.util.List;

public interface CarManager {
    void init(); // 集配初始化
    List<Car> listBusyCars();
    Car getCar(String carId);
    void pushCar(Car car, String targetList);
    void changeStatus(String carId, String newStatus) throws Exception;
}
