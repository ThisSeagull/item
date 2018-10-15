package com.cq.start.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cq.start.domain.LoginTickets;
import com.cq.start.domain.SystemUser;
import com.cq.start.mapper.LoginTicketsMapper;
import com.cq.start.mapper.SystemUserMapper;
import com.cq.start.response.Result;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.UUID;

/*import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;*/

@RestController
@RequestMapping("/bops")
public class SystemUserController extends BaseController {
    /*@Autowired
    private JavaMailSender mailSender; //自动注入的Bean*/
    /* @Value("${spring.mail.username}")
    private String Sender; //读取*/
    @Resource
    private SystemUserMapper systemUserMapper;
    @Resource
    private LoginTicketsMapper loginTicketsMapper;

    @RequestMapping(value = "/addSystemUser",method = RequestMethod.POST)
    public @ResponseBody Result addUser(HttpServletRequest request,HttpServletResponse response){
        Result r = new Result();
        SystemUser user = new SystemUser();
        String loginName = request.getParameter("loginName");
        String nickName = request.getParameter("nickName");
        String password = request.getParameter("password");
        try{
            if(StringUtils.isBlank(loginName) || StringUtils.isBlank(nickName)|| StringUtils.isBlank(password)){
                return r.failure(101,"参数错误");
            }
            QueryWrapper<SystemUser> q = new QueryWrapper();
            q.eq("login_name",loginName);
            SystemUser u = systemUserMapper.selectOne(q);
            if(u != null ){
                return r.failure(102,"该用户已存在，请更换登录名");
            }
            user.setLoginName(loginName);
            user.setPassword(DigestUtils.md5Hex(password));
            user.setNickName(nickName);
            int result =  systemUserMapper.insert(user);
            if(result>0){
                return r.success("注册成功");
            }else{
                return r.failure(1,"注册失败");
            }
        }
        catch (Exception e){
            return r.failure(1,"注册失败");
        }
    }

    @RequestMapping(value = "/adminLogin",method = RequestMethod.POST)
    public @ResponseBody Result login(HttpServletRequest request,HttpServletResponse response){
        Result r = new Result();
        try{
            String loginName = request.getParameter("loginName");
            String password = request.getParameter("password");
            if(StringUtils.isBlank(loginName) ||  StringUtils.isBlank(password)){
                return r.failure(101,"参数错误");
            }
            QueryWrapper<SystemUser> q = new QueryWrapper();
            q.eq("login_name",loginName);
            SystemUser u = systemUserMapper.selectOne(q);
            if(u == null ){
                return r.failure(102,"用户不存在");
            }
            if(!StringUtils.equals(DigestUtils.md5Hex(password),u.getPassword())){
                return r.failure(102,"用户名或密码错误");
            }
            String tickets = addLoginTicket(u.getId());
            Cookie cookie = new Cookie("ticket",tickets);
            cookie.setPath("/");
            response.addCookie(cookie);
            Cookie nickNameCookie = new Cookie("nickName", u.getNickName());
            nickNameCookie.setPath("/");
            response.addCookie(nickNameCookie);
            return r.success("登陆成功");
        }catch (Exception e){
            return r.success("系统错误");
        }

    }

    public String addLoginTicket(long user_id){
        LoginTickets ticket = new LoginTickets();
        ticket.setUserId(user_id);
        Date nowDate = new Date();
        nowDate.setTime(3600*24*100 + nowDate.getTime());
        ticket.setExpired(nowDate);
        ticket.setStatus(0);
        ticket.setTickets(UUID.randomUUID().toString().replaceAll("_",""));
        loginTicketsMapper.insert(ticket);
        return ticket.getTickets();
    }



}
