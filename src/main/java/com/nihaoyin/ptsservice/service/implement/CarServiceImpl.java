package com.nihaoyin.ptsservice.service.implement;

import com.nihaoyin.ptsservice.bean.Car;
import com.nihaoyin.ptsservice.service.implement.manager.ResourceManagerFactory;
import com.nihaoyin.ptsservice.service.interfaces.CarService;
import com.nihaoyin.ptsservice.service.interfaces.manager.ResourceManager;

import java.util.ArrayList;
import java.util.List;

public class CarServiceImpl implements CarService {
    private ResourceManager resourceManager = ResourceManagerFactory.getResourceManager();

    @Override
    public List<Car> listCar(String status) {
        if(status.equals("running")){
            return resourceManager.listRunningCar();
        }else if(status.equals(("waiting"))){
            return resourceManager.listWaitingCar("all");
        }else{
            return new ArrayList<Car>();
        }

    }

    @Override
    public int countCar(String status) {
        if(status.equals("running")){
            return resourceManager.listRunningCar().size();
        }else if(status.equals(("waiting"))){
            return resourceManager.listWaitingCar("all").size();
        }else{
            return new ArrayList<Car>().size();
        }
    }

    @Override
    public Car getCar(String carId) {
        return resourceManager.getCar(carId);
    }
}
