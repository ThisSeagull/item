package com.cq.start.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cq.start.domain.SystemUser;
import com.cq.start.domain.User;
import com.cq.start.domain.enums.UserStatus;
import com.cq.start.mapper.UserMapper;
import com.cq.start.response.Result;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
/*import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;*/
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

   /* @Autowired
    private UserRepository userRepository;*/
    /*@Autowired
    private JavaMailSender mailSender; //自动注入的Bean*/
    /* @Value("${spring.mail.username}")
    private String Sender; //读取*/
    @Resource
    private UserMapper userMapper;

    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    public @ResponseBody Result addUser(HttpServletRequest request){
        Result r = new Result();
        SystemUser user = new SystemUser();

        String loginName = request.getParameter("loginName");
        String nickName = request.getParameter("nickName");
        String password = request.getParameter("password");
        if(StringUtils.isBlank(loginName) || StringUtils.isBlank(nickName)|| StringUtils.isBlank(password)){
            return r.failure(101,"参数错误");
        }
        QueryWrapper<SystemUser> q = new QueryWrapper();
        q.eq("login_name",loginName);
        SystemUser u = userMapper.selectOne(q);
        if(u != null ){
            return r.failure(102,"该用户已存在，请更换登录名");
        }
        user.setLoginName(loginName);
        user.setPassword(DigestUtils.md5Hex(password));
        user.setNickName(nickName);
       int result =  userMapper.insert(user);
        if(result>0){
           return r.success("注册成功");
       }else{
           return r.failure(1,"注册失败");
       }

    }

 /*   @RequestMapping(value = "/sendEmail",method = RequestMethod.GET)
    public void sendMail(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(Sender);
        String [] arr =new String[]{"haiou@ibeidiao.com","244969436@qq.com"};
        message.setTo(arr); //自己给自己发送邮件
        message.setSubject("海鸥的邮件来啦111");
        message.setText("测试邮件内容");
        mailSender.send(message);
    }*/

}
