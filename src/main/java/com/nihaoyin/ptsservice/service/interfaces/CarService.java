package com.nihaoyin.ptsservice.service.interfaces;

import com.nihaoyin.ptsservice.bean.Car;

import java.util.List;

public interface CarService {
    List<Car> listCar(String status);
    int countCar(String status);
    Car getCar(String carId);
}
