package com.nihaoyin.ptsservice.controller;

import com.nihaoyin.ptsservice.bean.Order;
import com.nihaoyin.ptsservice.service.implement.OrderServiceImpl;
import com.nihaoyin.ptsservice.service.interfaces.OrderService;
import com.nihaoyin.ptsservice.service.interfaces.UserService;
import com.nihaoyin.ptsservice.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/order")
public class OrderController {
    private final static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    OrderService orderService;

    @GetMapping("/count")
    public synchronized Object handleCountOrder(@RequestParam(name="status", defaultValue = "all")String status){
        int ret;
        switch (status) {
            case "waiting":
                ret = orderService.countWaitingOrder();
                break;
            case "running":
                ret = orderService.countRunningOrder();
                break;
            case "finished":
                ret = orderService.countFinishedOrder();
                break;
            default:
                return JsonUtil.failure("status不正确");
        }
        return JsonUtil.success(ret);
    }

    @GetMapping("/get")
    public synchronized Object handleGetOrder(@RequestParam("orderid") int orderId){
        try{
            Order order = orderService.getOrder(orderId);
            if(order == null){
                return JsonUtil.failure("该orderId不存在");
            }
            return JsonUtil.success(order);
        }catch(Exception e){
            return JsonUtil.failure(e.toString());
        }
    }

    @PostMapping("/place")
    public synchronized Object handlePlaceOrder(@RequestBody Order order){
        logger.info("新订单  {}", order);
        try{
            orderService.placeOrder(order);
            return JsonUtil.success();
        }catch (Exception e){
            return JsonUtil.failure(e.toString());
        }
    }

    @GetMapping("/list")
    public Object handleListOrder(@RequestParam("status") String status,
                                  @RequestParam(name="base", defaultValue="0") int base,
                                  @RequestParam(name="offset", defaultValue = "100")int offset){
        switch (status) {
            case "waiting":
                return JsonUtil.success(orderService.listWaitingOrder());
            case "running":
                return JsonUtil.success(orderService.listRunningOrder());
            case "finished":
                return JsonUtil.success(orderService.listFinishedOrder(base, offset));
            default:
                return JsonUtil.failure("状态错误");
        }
    }

    @PutMapping("/finish")
    public Object handleFinishOrder(@RequestParam("orderid") int orderId){
        logger.info("订单完成 {}", orderId);
        try{
            orderService.finishOrder(orderId);
            return JsonUtil.success();
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.failure(e.toString());
        }
    }

    @DeleteMapping("/delete")
    public Object handleDeleteOrder(@RequestParam("orderid") int orderId){
        try{
            orderService.deleteOrder(orderId);
            return JsonUtil.success();
        }catch (Exception e){
            return JsonUtil.failure(e.toString());
        }

    }
}
