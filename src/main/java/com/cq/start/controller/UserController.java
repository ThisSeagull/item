package com.cq.start.controller;


import com.cq.start.domain.User;
import com.cq.start.domain.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/front")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/addUser",method = RequestMethod.GET)
    public String addUser(){
        User u = new User();
        u.setAge(16);
        u.setName("66");
        u.setEmail("99");
        u.setStatus(UserStatus.Enable.getCode());
        userRepository.save(u);
        return "添加成功";
    }
}
