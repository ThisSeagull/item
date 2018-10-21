package com.cq.start.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cq.start.domain.User;
import com.cq.start.domain.enums.Status;
import com.cq.start.domain.querydomain.UserQuery;
import com.cq.start.mapper.SystemUserMapper;
import com.cq.start.mapper.UserMapper;
import com.cq.start.response.Result;
import com.cq.start.service.UserService;
import com.cq.start.tools.BaseValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {

    @Resource
    private SystemUserMapper systemUserMapper;

    @Resource
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

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

            QueryWrapper<User> u = new QueryWrapper();
            u.eq("mobile",user.getMobile());
            List<User> userList = userMapper.selectList(u);
            if(CollectionUtils.isNotEmpty(userList)){
                return r.failure(103,"手机号已被注册,请换个手机号再试试");
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


    @RequestMapping(value = "/editUser",method = RequestMethod.POST)
    public @ResponseBody Result editUser(User user,HttpServletRequest request){
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
            User oldUser = userMapper.selectById(user.getId());
            if (!StringUtils.equals(user.getMobile(),oldUser.getMobile())){
                QueryWrapper<User> qu = new QueryWrapper();
                qu.eq("mobile",user.getMobile());
                List<User> userList = userMapper.selectList(qu);
                if(CollectionUtils.isNotEmpty(userList)){
                    return r.failure(102,"手机号已存在，请更换手机号");
                }
            }
            user.setModifyTime(new Date());
            Integer result = userMapper.updateById(user);
            if(result >0){
                return r.success("修改用户成功");
            }else{
                return r.failure(1,"修改用户失败");
            }

        }catch (Exception e){
            logger.error("修改客户失败请联系管理员",e);
            return r.failure(1,"修改客户失败请联系管理员");
        }
    }

    @RequestMapping(value = "/getUserById")
    public @ResponseBody Result getUserById(HttpServletRequest request){
        Result r = new Result();
        try {
            String id =request.getParameter("id");
            if(!StringUtils.isNumeric(id)){
                return r.failure(101,"参数错误");
            }
            QueryWrapper<User> qw =new QueryWrapper<>();
            qw.eq("id",id);
            User user =  userMapper.selectOne(qw);
            return r.success("获得用户成功").setData(user);

        }catch (Exception e){
            logger.error("获得用户失败请联系管理员",e);
            return r.failure(1,"获得用户失败请联系管理员");
        }
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public @ResponseBody Result getUserList(@Param("uq") UserQuery uq, HttpServletRequest request){
        Result r = new Result();
        try {
            QueryWrapper<User> qw  = new QueryWrapper<>();
            IPage<User> u = new Page<>(uq.getCurrent(),uq.getSize());

            u.setTotal(userMapper.selectCount(qw));

            IPage<User> list =  userMapper.selectPage(u,qw);
            return r.success("获得用户列表成功").setData(list);

        }catch (Exception e){
            logger.error("获得用户列表失败请联系管理员",e);
            return r.failure(1,"获得用户列表失败请联系管理员");
        }
    }

    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public @ResponseBody Result userList(UserQuery uq ,HttpServletRequest request){
        Result r = new Result();
        try {
            QueryWrapper<User> qw  = new QueryWrapper<>();
            IPage<User> u = new Page<>(uq.getCurrent(),uq.getSize());

            if(StringUtils.isNotBlank(uq.getName())){
                qw.like("name",uq.getName());
            }
            if(StringUtils.isNotBlank(uq.getMobile())){
                qw.like("mobile",uq.getMobile());
            }
            IPage<User> list =  userMapper.selectPage(u,qw);
            return r.success("获得用户列表成功").setData(list);

        }catch (Exception e){
            logger.error("获得用户列表失败请联系管理员",e);
            return r.failure(1,"获得用户列表失败请联系管理员");
        }
    }

    /**
     * 启用或停用 客户
     * @param user
     * @param request
     * @return
     */
    @RequestMapping(value = "/editUserStatus",method = RequestMethod.POST)
    public @ResponseBody Result editUserStatus(User user ,HttpServletRequest request){
        Result r = new Result();
        try {
            if(user.getStatus()!= Status.Disabled.getCode() && user.getStatus()!=Status.Enable.getCode()){
                return r.failure(101,"参数错误");
            }
            user.setModifyTime(new Date());
            int result = 0;
            if(user.getStatus() ==Status.Disabled.getCode()){
                result =  userMapper.deleteById(user.getId());
            }else{
                result = userMapper.setEnableById(user.getId());
            }
            if(result == 1){
                return r.success("更新成功");
            }else{
                return r.failure(1,"更新用户状态失败,请联系管理员");
            }
        }catch (Exception e){
            logger.error("更新用户状态失败,请联系管理员",e);
            return r.failure(1,"更新用户状态失败，请联系管理员");
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
