package com.nihaoyin.ptsservice.controller;

import com.nihaoyin.ptsservice.bean.User;
import com.nihaoyin.ptsservice.util.JsonUtil;
import com.nihaoyin.ptsservice.dao.UserDao;
import com.nihaoyin.ptsservice.util.SHA256Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class LoginController {
    private final static Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserDao userdao;

    @RequestMapping("/login")
    public Object HandleLogin(@RequestBody User user){
        logger.info("{}登陆", user.getUsername());
        String hashedPassword = SHA256Util.getSHA256(user.getPassword());
        Integer userId = userdao.getUserId(user.getUsername(), hashedPassword);
        logger.info("用户Id是 {}", userId);
        if (userId== null){
            return JsonUtil.failure("用户名或密码错误");
        }else{
            return JsonUtil.success();
        }
    }

    @RequestMapping("/test")
    public String test(@RequestParam("input") String input){
        return input;
    }
}
