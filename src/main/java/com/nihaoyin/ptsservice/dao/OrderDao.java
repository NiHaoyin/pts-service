package com.nihaoyin.ptsservice.dao;

import com.nihaoyin.ptsservice.bean.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDao {
    void addOrder(Order newOrder);
    void deleteOrder(@Param("orderId") int orderId);
    void finishOrder(@Param("orderId") int orderId);
    Order getOrder(@Param("orderId") int orderId);
    List<Order> listOrder(@Param("isFinished") boolean isFinished,
                          @Param("base") int base,
                          @Param("offset") int offset);
    Integer getNextOrderId();

}
