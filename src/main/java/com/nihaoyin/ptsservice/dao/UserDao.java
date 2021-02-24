package com.nihaoyin.ptsservice.dao;

import com.nihaoyin.ptsservice.bean.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    public Integer getUserId(@Param("username") String username,
                             @Param("password") String password);

    public Integer getNextUserId();

    public boolean getUserStatus(@Param("id") int id);

    public void addUser(User newUser);

    public void activateUser(@Param("id") int id);

    public void deactivateUser(@Param("id") int id);

    public void activateAllUser();

    public List<User> listUser();
}
