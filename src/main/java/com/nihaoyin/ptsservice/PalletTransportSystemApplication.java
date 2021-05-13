package com.nihaoyin.ptsservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin()
@MapperScan("com/nihaoyin/ptsservice/dao")
//@ComponentScan(basePackages = {"com.nihaoyin.ptsservice.dao"})
public class PalletTransportSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(PalletTransportSystemApplication.class, args);
    }
}
