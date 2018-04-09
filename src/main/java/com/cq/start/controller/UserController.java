package com.cq.start.controller;


import com.cq.start.domain.User;
import com.cq.start.domain.enums.UserStatus;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/front")
public class UserController extends BaseController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender; //自动注入的Bean

    @Value("${spring.mail.username}")
    private String Sender; //读取

    @RequestMapping(value = "/addUser",method = RequestMethod.GET)
    public String addUser(){
        User u = new User();
        u.setAge(16);
        u.setName("66");
        u.setEmail("99");
        u.setPassword(DigestUtils.md5Hex("111111"));
        u.setStatus(UserStatus.Enable.getCode());
        userRepository.save(u);
        log.error("添加用户成功~~~");
        return "添加成功";
    }

    @RequestMapping(value = "/sendEmail",method = RequestMethod.GET)
    public void sendMail(){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(Sender);
        String [] arr =new String[]{"haiou@ibeidiao.com","244969436@qq.com"};  ;
        message.setTo(arr); //自己给自己发送邮件
        message.setSubject("海鸥的邮件来啦");
        message.setText("测试邮件内容");
        mailSender.send(message);
    }

}
