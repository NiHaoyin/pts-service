package com.nihaoyin.ptsservice.controller;

import com.nihaoyin.ptsservice.bean.User;
import com.nihaoyin.ptsservice.service.interfaces.UserService;
import com.nihaoyin.ptsservice.util.JsonUtil;
import com.nihaoyin.ptsservice.dao.UserDao;
import com.nihaoyin.ptsservice.util.SHA256Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserDao userdao;

    @Autowired
    UserService userService;

    private static int nextUserId;

    @RequestMapping("/login")
    public Object HandleLogin(@RequestBody User user){
        logger.info("{}登陆", user.getUsername());
        Integer userId = userService.getUserId(user.getUsername(), user.getPassword());
        if (userId== null){
            return JsonUtil.failure("用户名或密码错误");
        }else{
            return JsonUtil.success();
        }
    }

    @GetMapping("/list")
    public Object HandleList(@RequestParam("base") int base,
                             @RequestParam(name="offset", defaultValue = "10") int offset){
        logger.info("查询用户, base={}, offset={}", base, offset);
        if (base < 0 || offset < 0){
            return JsonUtil.failure("参数不正确");
        }
        List<User> res = userService.listUser(base, offset);
        return JsonUtil.success(res);
    }

    @GetMapping("/count")
    public Object HandleCount(){
        logger.info("查询用户数量");
        Integer userCount = userService.countUser();
        if (userCount != null){
            return JsonUtil.success(userCount);
        }else{
            return JsonUtil.failure("查询用户数量失败");
        }
    }

    @PutMapping("/activate")
    public Object HandleActivate(@RequestParam("userid") int userid,
                                 @RequestParam("active") boolean active){
        logger.info("{} {}", userid, active);

        boolean isSuccess;
        if (active){
            isSuccess = userService.activateUser(userid);
        }else{
            isSuccess = userService.deactivateUser(userid);
        }
        if (isSuccess){
            return JsonUtil.success();
        }else{
            return JsonUtil.failure("用户状态修改失败");
        }
    }

    @PutMapping("/activateall")
    public Object HandleActivateAll(){
        logger.info("activate all users");
        boolean isSuccess = userService.activateAllUser();
        if (isSuccess){
            return JsonUtil.success();
        }else{
            return JsonUtil.failure("激活所有用户失败");
        }
    }

    @PostMapping("/add")
    public Object HandleAdd(@RequestBody User newUser){
        logger.info("添加用户  {}", newUser);
        if (newUser == null){
            return JsonUtil.failure("新用户为空");
        }
        boolean isSuccess = userService.addUser(newUser);
        if(isSuccess){
            return JsonUtil.success();
        }else{
            return JsonUtil.failure("增加用户失败");
        }
    }

    @DeleteMapping("/delete")
    public Object HandleDelete(@RequestParam("userid") int userId){
        boolean isSuccess = userService.deleteUser(userId);
        if(isSuccess){
            return JsonUtil.success();
        }
        return JsonUtil.failure("删除失败");
    }

    @RequestMapping("/test")
    public Object Test(HttpSession session){
        logger.info("this is test");
        if (session.getAttribute("user") == null){
            session.setAttribute("user", "nhy");
            return session.getId();
        }
        DemoController demoController = new DemoController();
        try{
            demoController.pushToWeb("hello", "10");
        }catch (Exception e){
            e.printStackTrace();
        }
        return session.getAttribute("user");
    }

    @RequestMapping("/test2/{id}")
    public Object Test2(@PathVariable("id") int id) throws InterruptedException {
        logger.info("id{} come in", id);
        int ret = userService.Test();
        if(id == 1){
            Thread.sleep(10000);
            return JsonUtil.failure("id=1");
        }

//        Thread.sleep(10000);
        logger.info("id{} come out", id);
        return JsonUtil.success(ret);
    }
}
