package com.nihaoyin.ptsservice.controller;

import com.nihaoyin.ptsservice.bean.Order;
import com.nihaoyin.ptsservice.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class PlaceOrderController {
    private final static Logger logger = LoggerFactory.getLogger(PlaceOrderController.class);

    @PostMapping("/placeorder")
    public Object HandlePlaceOrder(@RequestBody Order order){
        logger.info("下单{}", order);
        return JsonUtil.success();
    }
}
