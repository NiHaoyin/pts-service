package com.nihaoyin.ptsservice.controller;

import com.nihaoyin.ptsservice.bean.Test;

final public class TestFactory {
    private static Test test;
    static{
        test = new Test();
    }
    public static Test getSessionFactory(){
        return test;//返回该对象
    }
}
