package com.nihaoyin.ptsservice.controller;

import com.nihaoyin.ptsservice.bean.Car;
import com.nihaoyin.ptsservice.service.implement.CarServiceImpl;
import com.nihaoyin.ptsservice.service.interfaces.CarService;
import com.nihaoyin.ptsservice.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/car")
public class CarController {
    private final static Logger logger = LoggerFactory.getLogger(TrayController.class);
    private final CarService carService = new CarServiceImpl();

    @GetMapping("/get")
    public Object handleGetCar(@RequestParam("carid") String carId){
        Car car = carService.getCar(carId);
        if(car == null){
            return JsonUtil.failure("carId不存在");
        }else{
            return JsonUtil.success(car);
        }
    }

    @GetMapping("/list")
    public Object handleListCar(@RequestParam("status") String status){
        if(status.equals("running") || status.equals("waiting")){
            return JsonUtil.success(carService.listCar(status));
        }else{
            return JsonUtil.failure("参数错误");
        }
    }

    @GetMapping("/count")
    public Object handleCountCar(@RequestParam("status") String status){
        if(status.equals("running") || status.equals("waiting")){
            return JsonUtil.success(carService.countCar(status));
        }else{
            return JsonUtil.failure("参数错误");
        }
    }
}
