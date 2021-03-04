package com.nihaoyin.ptsservice.service.interfaces;

import com.nihaoyin.ptsservice.bean.User;

import java.util.List;

public interface UserService {
    Integer getUserId(String username, String password);
    List<User> listUser(int base, int offset);
    Integer countUser();
    boolean activateUser(int userId);
    boolean deactivateUser(int userId);
    boolean activateAllUser();
    boolean addUser(User newUser);
    boolean deleteUser(int userId);
}
