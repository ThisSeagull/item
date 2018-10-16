package com.cq.start.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cq.start.domain.LoginTickets;
import com.cq.start.domain.SystemUser;
import com.cq.start.domain.enums.Status;
import com.cq.start.mapper.LoginTicketsMapper;
import com.cq.start.mapper.SystemUserMapper;
import com.cq.start.response.Result;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.util.Date;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/bops")
public class SystemUserController extends BaseController {
    @Resource
    private SystemUserMapper systemUserMapper;
    @Resource
    private LoginTicketsMapper loginTicketsMapper;

    @RequestMapping(value = "/addSystemUser",method = RequestMethod.POST)
    public @ResponseBody Result addUser(HttpServletRequest request,HttpServletResponse response){
        Result r = new Result();
        SystemUser systemUser = new SystemUser();
        String loginName = request.getParameter("loginName");
        String nickName = request.getParameter("nickName");
        String password = request.getParameter("password");
        try{
            if(StringUtils.isBlank(loginName) || StringUtils.isBlank(nickName)|| StringUtils.isBlank(password)){
                return r.failure(101,"参数错误");
            }
            if(loginName.length() > 64){
                return r.failure(102,"登录名长度超出限制");
            }
            if(nickName.length()> 64){
                return r.failure(102,"昵称长度超出限制");
            }
            if(password.length() > 64){
                return r.failure(102,"密码长度超出限制");
            }
            QueryWrapper<SystemUser> q = new QueryWrapper();
            q.eq("login_name",loginName);
            SystemUser u = systemUserMapper.selectOne(q);
            if(u != null ){
                return r.failure(102,"该用户已存在，请更换登录名");
            }
            systemUser.setLoginName(loginName);
            systemUser.setPassword(DigestUtils.md5Hex(password));
            systemUser.setNickName(nickName);
            systemUser.setCreateTime(new Date());
            systemUser.setModifyTime(new Date());
            int result =  systemUserMapper.insert(systemUser);
            if(result>0){
                return r.success("注册成功");
            }else{
                return r.failure(1,"注册失败");
            }
        }catch (Exception e){
            logger.error("注册失败请联系管理员",e);
            return r.failure(1,"注册失败请联系管理员");
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
            logger.error("/adminLogin登陆出错",e);
            return r.success("登录出错请联系管理员");
        }
    }

    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    public @ResponseBody Result logout(HttpServletRequest request,HttpServletResponse response){
        Result r = new Result();
        try{
            String tickets = null;
            if(request.getCookies() != null){
                for(Cookie cookie : request.getCookies()){
                    if(cookie.getName().equals("ticket")){
                        tickets = cookie.getValue();
                        break;
                    }
                }
            }
            if(StringUtils.isNotBlank(tickets)){
                QueryWrapper<LoginTickets> q = new QueryWrapper();
                q.eq("tickets",tickets);
                LoginTickets u = loginTicketsMapper.selectOne(q);
                u.setStatus(Status.Disabled.getCode());
                loginTicketsMapper.updateById(u);
            }
            return r.success("退出成功");
        }catch (Exception e){
            logger.error("登出出错",e);
            return r.success("系统错误,请联系管理员");
        }
    }

    public String addLoginTicket(long user_id){
        LoginTickets ticket = new LoginTickets();
        ticket.setUserId(user_id);
        Date nowDate = new Date();
        nowDate.setTime(3600*24*100 + nowDate.getTime());
        ticket.setExpired(nowDate);
        ticket.setStatus(Status.Enable.getCode());
        ticket.setTickets(UUID.randomUUID().toString().replaceAll("_",""));
        loginTicketsMapper.insert(ticket);
        return ticket.getTickets();
    }



}
