package com.cq.start.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cq.start.domain.LoginTickets;
import com.cq.start.mapper.LoginTicketsMapper;
import com.cq.start.service.CommonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Service
public class CommonServiceImpl implements CommonService {

    @Resource
    private LoginTicketsMapper loginTicketsMapper;

    @Override
    public Long getLoginUserId(HttpServletRequest request) throws  Exception{
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

    @Override
    public String createOrderCode(String prefix) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//定义格式，不显示毫
        Date date = new Date();
        String str = df.format(date);
        return str +prefix+generateShortUuid();
    }
    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };

    public static String generateShortUuid (){
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 3; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }
}
