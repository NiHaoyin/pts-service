package com.nihaoyin.ptsservice.controller;

import com.nihaoyin.ptsservice.bean.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/test")
public class TestController {
    Test test = TestFactory.getSessionFactory();

    @GetMapping("/add")
    public int add() {
        System.out.println(test.x);
        return test.x++;
    }
}
