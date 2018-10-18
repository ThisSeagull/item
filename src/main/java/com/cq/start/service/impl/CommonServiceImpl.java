package com.cq.start.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cq.start.domain.LoginTickets;
import com.cq.start.mapper.LoginTicketsMapper;
import com.cq.start.service.CommonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Service
public class CommonServiceImpl implements CommonService {

    @Resource
    private LoginTicketsMapper loginTicketsMapper;

    @Override
    public long getLoginUserId(HttpServletRequest request) throws  Exception{
        String tickets = null;
        if(request.getCookies() != null){
            for(Cookie cookie : request.getCookies()){
                if(cookie.getName().equals("ticket")){
                    tickets = cookie.getValue();
                    break;
                }
            }
        }
        QueryWrapper<LoginTickets> ltqw = new QueryWrapper<>();

        ltqw.eq("tickets",tickets);
        LoginTickets s = loginTicketsMapper.selectOne(ltqw);
        return s.getUserId();
    }
}
