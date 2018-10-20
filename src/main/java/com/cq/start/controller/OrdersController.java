package com.cq.start.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.cq.start.domain.Orders;
import com.cq.start.domain.Samples;
import com.cq.start.domain.SystemUser;
import com.cq.start.domain.User;
import com.cq.start.domain.enums.InvoiceStatus;
import com.cq.start.mapper.OrdersMapper;
import com.cq.start.mapper.SamplesMapper;
import com.cq.start.mapper.SystemUserMapper;
import com.cq.start.mapper.UserMapper;
import com.cq.start.response.Result;
import com.cq.start.service.CommonService;
import com.cq.start.tools.BaseValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/orders")
public class OrdersController extends BaseController{

    @Autowired
    private CommonService commonService;
    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private SamplesMapper samplesMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private SystemUserMapper systemUserMapper;

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public @ResponseBody
    Result add(Orders o, HttpServletRequest request){
        Date d = new Date();
        Result r = new Result();
        try {
            if(o.getBelongUserId() == null){
                return r.failure(101,"用户选择错误");
            }
            if(o.getSampleId() == null){
                return r.failure(101,"产品选择错误");
            }
            if(o.getRealPrice() == null ){
                return r.failure(101,"实际金额不能为空");
            }
            if(!StringUtils.isNumeric(o.getRealPrice().toString())){
                return r.failure(101,"实际单价格式错误，请重新输入");
            }
            if(o.getNum() == null){
                return r.failure(101,"数量不能为空，请重新输入");
            }
            if(o.getInvoiceStatus() ==null){
                return r.failure(101,"请选择开票状态");
            }
            if(o.getInvoiceStatus() != InvoiceStatus.Wanted.getCode()  && o.getInvoiceStatus() != InvoiceStatus.Unwanted.getCode()){
                return r.failure(101,"发票状态选择错误");
            }
            if(o.getFreight() != null && !StringUtils.isNumeric(o.getFreight().toString())){
                return r.failure(101,"运费格式错误，请重新输入");
            }
            o.setCreateUserId(commonService.getLoginUserId(request));
            o.setCode(commonService.createOrderCode());
            o.setCreateTime(d);
            o.setModifyTime(d);
            o.setTotalPrice(o.getRealPrice() *o.getNum()+o.getFreight());
            int result = ordersMapper.insert(o);
            if(result >0){
                return r.success("创建订单成功").setData(o);
            }else{
                return r.failure(1,"创建订单失败");
            }
        }catch (Exception e){
            logger.error("创建订单失败请联系管理员",e);
            return r.failure(1,"创建订单失败请联系管理员");
        }
    }



    @RequestMapping(value = "/deleteById",method = RequestMethod.POST)
    public @ResponseBody
    Result deleteById(HttpServletRequest request){
        Date d = new Date();
        Result r = new Result();
        try {
            String id = request.getParameter("id");
            if(!StringUtils.isNumeric(id)){
                return r.failure(101,"订单id错误");
            }

            int result = ordersMapper.deleteById(Long.valueOf(id));

            if(result >0){
                return r.success("删除成功");
            }else{
                return r.failure(1,"创建删除失败");
            }
        }catch (Exception e){
            logger.error("订单删除失败请联系管理员",e);
            return r.failure(1,"订单删除失败请联系管理员");
        }
    }

    @RequestMapping(value = "/getDetailById",method = RequestMethod.POST)
    public @ResponseBody
    Result getDetailById(HttpServletRequest request){
        Date d = new Date();
        Result r = new Result();
        Map m = new HashMap();
        try {
            String id = request.getParameter("id");
            if(!StringUtils.isNumeric(id)){
                return r.failure(101,"订单id错误");
            }
            Orders o = ordersMapper.selectById(Long.valueOf(id));

            if(o == null){
                return r.failure(101,"参数错误");
            }
            m.put("orderInfo",o);
            Samples s =samplesMapper.selectById(o.getSampleId());
            m.put("sampleInfo",s);

            User u= userMapper.selectById(o.getBelongUserId());
            m.put("userInfo",u);
            SystemUser su =systemUserMapper.selectById(o.getCreateUserId());
            m.put("systemUserInfo",u);

            return r.success("获取订单详情成功").setData(m);
        }catch (Exception e){
            logger.error("订获取订单详情失败请联系管理员",e);
            return r.failure(1,"获取订单详情失败请联系管理员");
        }
    }









}
