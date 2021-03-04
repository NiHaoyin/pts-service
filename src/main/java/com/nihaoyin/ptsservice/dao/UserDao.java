package com.nihaoyin.ptsservice.dao;

import com.nihaoyin.ptsservice.bean.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    Integer getUserId(@Param("username") String username,
                             @Param("password") String password);

    Integer getNextUserId();

    Integer countUsername(@Param("username")String username);

    boolean getUserStatus(@Param("id") int id);

    void addUser(User newUser);

    void changeStatus(@Param("id") int id, boolean status);

    void activateAllUser();

    List<User> listUser(@Param("base") int base, @Param("offset") int offset);

    Integer countUser();

    void deleteUser(@Param("id") int id);
}
