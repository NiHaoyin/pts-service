package com.nihaoyin.ptsservice.util;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;

//对响应结果进行统一封装
//2021/2/4

public class JsonUtil {
    private static final Boolean SUCCESS = true;
    private static final Boolean FAILURE = false;
    private static final String IS_SUCCESS = "isSuccess";
    private static final String ERROR_MESSAGE = "errorMessage";

    public static String success(Object resultData){
        HashMap<String, Object> ret = new HashMap<>();
        ret.put("data", resultData);
        ret.put(IS_SUCCESS, SUCCESS);
        return JSON.toJSONString(ret);
    }

    public static String success(){
        HashMap<String, Object> ret = new HashMap<>();
        ret.put("data", "");
        ret.put(IS_SUCCESS, SUCCESS);
        return JSON.toJSONString(ret);
    }

    public static String failure(Object resultData){
        HashMap<String, Object> ret = new HashMap<>();
        ret.put("data", resultData);
        ret.put(IS_SUCCESS, FAILURE);
        return JSON.toJSONString(ret);
    }

    public static String failure(String errorMessage){
        HashMap<String, Object> ret = new HashMap<>();
        ret.put("data", "");
        ret.put(IS_SUCCESS, FAILURE);
        ret.put(ERROR_MESSAGE, errorMessage);
        return JSON.toJSONString(ret);
    }



}
