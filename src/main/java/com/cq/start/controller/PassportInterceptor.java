package com.cq.start.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cq.start.domain.LoginTickets;
import com.cq.start.domain.SystemUser;
import com.cq.start.mapper.LoginTicketsMapper;
import com.cq.start.mapper.SystemUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Resource
    private SystemUserMapper systemUserMapper;
    @Resource
    private LoginTicketsMapper loginTicketsMapper;


    /*@Autowired
    HostHolder hostHolder;

    //判断然后进行用户拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tickets = null;
        if(request.getCookies() != null){
            for(Cookie cookie : request.getCookies()){
                if(cookie.getName().equals("ticket")){
                    tickets = cookie.getValue();
                    break;
                }
            }
        }

        if(tickets != null ){
            loginTickets loginTickets  = lTicketsDAO.selectByTicket(tickets);
            if(loginTickets == null || loginTickets.getExpired().before(new Date()) || loginTickets.getStatus() != 0){
                return true;
            }

            User user = uDAO.selectById(loginTickets.getUserId());
            hostHolder.setUser(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //就是为了能够在渲染之前所有的freemaker模板能够访问这个对象user，就是在所有的controller渲染之前将这个user加进去
        if(modelAndView != null){
            //这个其实就和model.addAttribute一样的功能，就是把这个变量与前端视图进行交互 //就是与header.html页面的user对应
            modelAndView.addObject("user",hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();   //当执行完成之后呢需要将变量清空
    }*/

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String tickets = null;
        if(request.getCookies() != null){
            for(Cookie cookie : request.getCookies()){
                if(cookie.getName().equals("ticket")){
                    tickets = cookie.getValue();
                    break;
                }
            }
        }
        if(tickets != null ){
            QueryWrapper<LoginTickets> lt = new QueryWrapper();
            lt.eq("tickets",tickets);
            LoginTickets loginTickets = loginTicketsMapper.selectOne(lt);
            if(loginTickets == null ||  loginTickets.getStatus() != 0){
                httpServletResponse.setStatus(403);
                return false;

            }

            QueryWrapper<SystemUser> su = new QueryWrapper();
            SystemUser user = systemUserMapper.selectById(loginTickets.getUserId());
            //hostHolder.setUser(user);
            return true;
        }
        httpServletResponse.setStatus(403);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}