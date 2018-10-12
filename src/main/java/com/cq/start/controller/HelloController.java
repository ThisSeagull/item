package com.cq.start.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;




@RestController
public class HelloController extends BaseController{

    @Value("${name}")
    private String name;

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String say(){
        return "hello "+name;
    }
    @RequestMapping(value = "/hello1",method = RequestMethod.GET)
    public String say1(){
        return "hello 黄小英";
    }




}
