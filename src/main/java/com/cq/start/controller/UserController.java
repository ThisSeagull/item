package com.cq.start.controller;


import com.cq.start.domain.User;
import com.cq.start.mapper.SystemUserMapper;
import com.cq.start.mapper.UserMapper;
import com.cq.start.response.Result;
import com.cq.start.tools.BaseValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private SystemUserMapper systemUserMapper;

    @Resource
    private UserMapper userMapper;

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public @ResponseBody Result registerUser(User user,HttpServletRequest request){
        Result r = new Result();
        try {
            if(StringUtils.isBlank(user.getName())){
                return r.failure(101,"姓名不能为空");
            }
            if(StringUtils.isBlank(user.getMobile())){
                return r.failure(101,"手机号不能为空");
            }
            if(!BaseValidator.MobileMatch(user.getMobile())){
                return r.failure(102,"手机号校验失败");
            }
            user.setCreateTime(new Date());
            user.setModifyTime(new Date());
            Integer result = userMapper.insert(user);
            if(result >0){
                return r.success("注册用户成功");
            }else{
                return r.failure(1,"注册用户失败");
            }

        }catch (Exception e){
            logger.error("注册客户失败请联系管理员",e);
            return r.failure(1,"注册客户失败请联系管理员");
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
