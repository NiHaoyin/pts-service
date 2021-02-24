package com.nihaoyin.ptsservice.controller;

import com.nihaoyin.ptsservice.bean.User;
import com.nihaoyin.ptsservice.util.JsonUtil;
import com.nihaoyin.ptsservice.dao.UserDao;
import com.nihaoyin.ptsservice.util.SHA256Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserDao userdao;

    private static int nextUserId;

    @RequestMapping("/login")
    public Object HandleLogin(@RequestBody User user){
        logger.info("{}登陆", user.getUsername());

        // 数据库中存储的是密码的哈希值，因此要对password做一次哈希再进行查询
        String hashedPassword = SHA256Util.getSHA256(user.getPassword());
        Integer userId = userdao.getUserId(user.getUsername(), hashedPassword);
        logger.info("用户Id是 {}", userId);
        if (userId== null){
            return JsonUtil.failure("用户名或密码错误");
        }else{
            return JsonUtil.success();
        }
    }

    @RequestMapping("/list")
    public Object HandleList(){
        logger.info("查询所有用户");
        List<User> res = userdao.listUser();

        return JsonUtil.success(res);
    }

    @RequestMapping("/activate")
    public Object HandleActivate(@RequestParam("userid") int userid,
                                 @RequestParam("active") boolean active){
        logger.info("{} {}", userid, active);

        boolean userStatus = userdao.getUserStatus(userid);
        if (userStatus == active){
            return JsonUtil.failure("用户状态不需要修改");
        }
        if (active){
            userdao.activateUser(userid);
        }else{
            userdao.deactivateUser(userid);
        }
        return JsonUtil.success();
    }

    @RequestMapping("/activateall")
    public Object HandleActivateAll(){
        logger.info("activate all users");
        userdao.activateAllUser();
        return JsonUtil.success();
    }

    // 先判断用户名是否重复，如果没重复就对密码做哈希，设置userid插入数据库
    // userid为数据库中最大的userid+1
    @RequestMapping("/add")
    public Object HandleAdd(@RequestBody User newUser){
        logger.info("添加用户  {}", newUser);
        Integer userId = userdao.getUserId(newUser.getUsername(), null);

        if (userId != null){
            return JsonUtil.failure("用户名已存在");
        }else{
            newUser.setId(Integer.toString(userdao.getNextUserId()));
            newUser.setPassword(SHA256Util.getSHA256(newUser.getPassword()));
            logger.info("{}", newUser);
            userdao.addUser(newUser);

            return JsonUtil.success();
        }
    }


}
