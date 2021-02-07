package com.nihaoyin.ptsservice.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
    public Integer getUserId(@Param("username") String username,
                         @Param("password") String password);


}
