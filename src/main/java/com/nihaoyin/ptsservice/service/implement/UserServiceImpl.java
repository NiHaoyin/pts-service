package com.nihaoyin.ptsservice.service.implement;

import com.nihaoyin.ptsservice.bean.User;
import com.nihaoyin.ptsservice.controller.UserController;
import com.nihaoyin.ptsservice.dao.UserDao;
import com.nihaoyin.ptsservice.service.interfaces.UserService;
import com.nihaoyin.ptsservice.util.JsonUtil;
import com.nihaoyin.ptsservice.util.SHA256Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userdao;

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /*
    ** @param username
    ** @param原始的password
    ** @return 数据库中匹配成功返回该用户的userId, 匹配失败返回null
    */
    @Override
    public Integer getUserId(String username, String password){
        String hashedPassword = SHA256Util.getSHA256(password);
        Integer userId = userdao.getUserId(username, hashedPassword);
        logger.info("用户id {} 登录", userId);
        return userId;
    }

    /*
    ** @param base
    ** @param offset
    ** @return List<User>
     */
    @Override
    public List<User> listUser(int base, int offset){
        logger.info("查询用户, base={}, offset={}", base, offset);
        return userdao.listUser(base, offset);
    }

    @Override
    public Integer countUser(){
        return userdao.countUser();
    }

    @Override
    public boolean activateUser(int userId){
        if (userId <= 0){
            return false;
        }
        userdao.changeStatus(userId, true);
        return true;
    }


    @Override
    public boolean deactivateUser(int userId) {
        if (userId <= 1){
            return false;
        }
        userdao.changeStatus(userId, false);
        return true;
    }

    @Override
    public boolean activateAllUser(){
        userdao.activateAllUser();
        return true;
    }

    // 先判断用户名是否重复，如果没重复就对密码做哈希，设置userid插入数据库
    // userid为数据库中最大的userid+1
    @Override
    public boolean addUser(User newUser){
        logger.info("新用户{}", newUser.toString());
        Integer count = userdao.countUsername(newUser.getUsername());
        if (count == null || count != 0){
            return false;
        }else{
            try{
                newUser.setId(Integer.toString(userdao.getNextUserId()));
                newUser.setPassword(SHA256Util.getSHA256(newUser.getPassword()));
                logger.info("{}", newUser);
                userdao.addUser(newUser);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    @Override
    public boolean deleteUser(int userId){
        if (userId == 1){
            return false;
        }
        try{
            userdao.deleteUser(userId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
