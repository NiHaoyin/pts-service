package com.nihaoyin.ptsservice.controller;

import com.nihaoyin.ptsservice.bean.Order;
import com.nihaoyin.ptsservice.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/order")
public class OrderController {
    private final static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @PostMapping("/place")
    public Object HandlePlaceOrder(@RequestBody Order order){
        logger.info("下单{}", order);
        return JsonUtil.success();
    }

}
