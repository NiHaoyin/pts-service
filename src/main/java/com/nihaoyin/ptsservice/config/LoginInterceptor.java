package com.nihaoyin.ptsservice.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

//public class LoginInterceptor implements  HandlerInterceptor{
//    /**
//     * 在请求处理之前进行调用（Controller方法调用之前）
//     */
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        //统一拦截（查询当前session是否存在user）(这里user会在每次登陆成功后，写入session)
//        response.setHeader("Access-Control-Expose-Headers", "token");// 服务器 headers 白名单，可以让客户端进行访问操作的属性
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.addHeader("Access-Control-Allow-Methods", "POST");
//        response.addHeader("Access-Control-Allow-Headers", "Accept");
//        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
//        Object username = request.getSession().getAttribute("USER_NAME");
//        System.out.println(username);
//        return username != null;//如果设置为false时，被请求时，拦截器执行到此处将不会继续操作
//        //如果设置为true时，请求将会继续执行后面的操作
//    }
//
//}
