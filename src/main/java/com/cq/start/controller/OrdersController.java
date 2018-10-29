package com.cq.start.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cq.start.domain.*;
import com.cq.start.domain.enums.DeliverProgress;
import com.cq.start.domain.enums.InvoiceProgress;
import com.cq.start.domain.enums.InvoiceStatus;
import com.cq.start.domain.enums.PaymentProgress;
import com.cq.start.domain.querydomain.OrderQuery;
import com.cq.start.mapper.*;
import com.cq.start.response.Result;
import com.cq.start.service.CommonService;
import com.cq.start.tools.BaseValidator;
import com.cq.start.tools.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    @Resource
    private OrdersDeliverRecordsMapper ordersDeliverRecordsMapper;


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
            if(StringUtils.isBlank(o.getDeliveryDate())){
                return r.failure(101,"请选择交货日期");
            }
            try {
                Date date=new Date();
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                String time=format.format(date);
                if(DateUtil.convertStringToDate("yyyy-MM-dd",o.getDeliveryDate()).getTime()<DateUtil.convertStringToDate("yyyy-MM-dd",time).getTime()){
                    return r.failure(101,"请选择交货日期不得小于今天");
                }
            }catch (Exception e){
                logger.error("新增订单日期转换错误",e);
                return r.failure(101,"日期转换错误，请联系管理员");
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
            Samples s =samplesMapper.selectByIdIgnoreStatus(o.getSampleId());
            m.put("sampleInfo",s);

            User u= userMapper.selectByIdIgnoreStatus(o.getBelongUserId());
            m.put("userInfo",u);
            SystemUser su =systemUserMapper.selectByIdIgnoreStatus(o.getCreateUserId());
            m.put("systemUserInfo",su);
            List<OrderDeliverRecords> ordList = ordersMapper.getOrderDeliverRecordsByOrderId(o.getId());
            if(o.getDeliverProgress() !=DeliverProgress.YES.getCode() && CollectionUtils.isNotEmpty(ordList)){//如果发货进度未完成 且有发货记录 设置为发货中
                o.setDeliverProgress(DeliverProgress.DELIVING.getCode() );
            }
            m.put("orderDeliversInfo",ordList);
            return r.success("获取订单详情成功").setData(m);
        }catch (Exception e){
            logger.error("订获取订单详情失败请联系管理员",e);
            return r.failure(1,"获取订单详情失败请联系管理员");
        }
    }

    @RequestMapping(value = "/list")
    public @ResponseBody
    Result list(@Param("oq") OrderQuery oq, HttpServletRequest request){
        Result r = new Result();
        try {
            IPage<Orders> page = new Page<>(oq.getCurrent(),oq.getSize());

            page.setRecords(ordersMapper.selectOrdersListPage(page,oq));
            return r.success("获取订单列表成功").setData(page);
        }catch (Exception e){
            logger.error("订获取订单列表失败请联系管理员",e);
            return r.failure(1,"获取订单列表失败请联系管理员");
        }
    }

    @RequestMapping(value = "/editOrder",method = RequestMethod.POST)
    public @ResponseBody
    Result editOrder(Orders o, HttpServletRequest request){
        Date d = new Date();
        Result r = new Result();
        try {
            if(o.getId()== null){
                return r.failure(101,"参数错误");
            }
            Orders oldOrders = ordersMapper.selectById(o.getId());
            Orders needUpdateOrder = new Orders();
            needUpdateOrder.setId(o.getId());
            if(StringUtils.isNotBlank(o.getDeliveryDate())){
                try {
                    if(DateUtil.convertStringToDate("yyyy-MM-dd",oldOrders.getDeliveryDate()).getTime() > DateUtil.convertStringToDate("yyyy-MM-dd",o.getDeliveryDate()).getTime()){
                        return r.failure(101,"新交付日期不能小于原来的交付日期");
                    }
                    needUpdateOrder.setDeliveryDate(o.getDeliveryDate());
                }catch (Exception e){
                    logger.error("修改订单时，交付日期转换错误，请联系管理员",e);
                    return r.failure(101,"修改订单时，交付日期转换错误，请联系管理员");
                }
            }
            if(o.getRealPrice() != null){
                if(!StringUtils.isNumeric(o.getRealPrice().toString())){
                    return r.failure(101,"实际单价格式错误，请重新输入");
                }
                if(oldOrders.getRealPrice() != o.getRealPrice()){
                    needUpdateOrder.setRealPrice(o.getRealPrice());
                }else{
                    needUpdateOrder.setRealPrice(oldOrders.getRealPrice());
                }
            }else{
                needUpdateOrder.setRealPrice(oldOrders.getRealPrice());
            }
            if(StringUtils.isNotBlank(o.getRemarks())){
                needUpdateOrder.setRemarks(o.getRemarks());
            }
            if(o.getDeliveryType()!=null ){
                needUpdateOrder.setDeliveryType(o.getDeliveryType());
            }
            if(o.getNum() != null){
                if(!StringUtils.isNumeric(o.getNum().toString())){
                    return r.failure(101,"数量格式错误,请重新输入");
                }
                if(o.getNum() != oldOrders.getNum()){
                    needUpdateOrder.setNum(o.getNum());
                }else{
                    needUpdateOrder.setNum(oldOrders.getNum());
                }
            }else{
                needUpdateOrder.setNum(oldOrders.getNum());
            }

            if(o.getInvoiceStatus() !=null){
                if(o.getInvoiceStatus() != InvoiceStatus.Wanted.getCode()  && o.getInvoiceStatus() != InvoiceStatus.Unwanted.getCode()){
                    return r.failure(101,"发票状态选择错误");
                }
                needUpdateOrder.setInvoiceStatus(o.getInvoiceStatus());
            }

            if(o.getFreight() != null){
                if(!StringUtils.isNumeric(o.getFreight().toString())){
                    return r.failure(101,"运费格式错误，请重新输入");
                }
                needUpdateOrder.setFreight(o.getFreight());
            }else{
                needUpdateOrder.setFreight(oldOrders.getFreight());
            }
            needUpdateOrder.setModifyTime(d);
            needUpdateOrder.setTotalPrice(needUpdateOrder.getRealPrice() *needUpdateOrder.getNum()+needUpdateOrder.getFreight());
            int result = ordersMapper.updateById(needUpdateOrder);
            if(result >0){
                return r.success("修改订单成功");
            }else{
                return r.failure(1,"修改订单失败");
            }
        }catch (Exception e){
            logger.error("修改订单失败请联系管理员",e);
            return r.failure(1,"修改订单失败请联系管理员");
        }
    }

    /**
     * 修改发货进度
     * @param request
     * @return
     */
    @RequestMapping(value = "/editDeliverProgress",method = RequestMethod.POST)
    public @ResponseBody
    Result editDeliverProgress(HttpServletRequest request){
        Result r = new Result();
        try {
            String id = request.getParameter("id");
                String deliverProgress = request.getParameter("deliverProgress");
            if(StringUtils.isBlank(id) || StringUtils.isBlank(deliverProgress)){
                return  r.failure(101,"参数错误");
            }
            if(!StringUtils.isNumeric(id)){
                return  r.failure(101,"订单错误");
            }
            if(!StringUtils.equals(DeliverProgress.NOTYET.getCode().toString(),deliverProgress) && !StringUtils.equals(DeliverProgress.YES.getCode().toString(),deliverProgress)){
                return  r.failure(101,"发货进度选择错误");
            }
            Map m =new HashMap();
            m.put("id",id);
            m.put("deliver_progress",deliverProgress);
            int result =ordersMapper.editDeliverProgressById(m);
            if(result >0){
                return r.success("修改发货状态成功");
            }else{
                return r.failure(1,"修改发货状态失败");
            }
        }catch (Exception e){
            logger.error("修改发货状态失败请联系管理员",e);
            return r.failure(1,"修改发货状态失败请联系管理员");
        }
    }
    /**
     * 修改发票进度
     * @param request
     * @return
     */
    @RequestMapping(value = "/editInvoiceProgress",method = RequestMethod.POST)
    public @ResponseBody
    Result editInvoiceProgress(HttpServletRequest request){
        Result r = new Result();
        try {
            String id = request.getParameter("id");
            String invoiceProgress = request.getParameter("invoiceProgress");
            String invoiceProgressRemarks = request.getParameter("invoiceProgressRemarks");
            if(StringUtils.isBlank(id) || StringUtils.isBlank(invoiceProgress) || StringUtils.isBlank(invoiceProgressRemarks)){
                return  r.failure(101,"参数错误");
            }
            if(!StringUtils.isNumeric(id)){
                return  r.failure(101,"订单错误");
            }
            if(!StringUtils.equals(InvoiceProgress.NOTYET.getCode().toString(),invoiceProgress) && !StringUtils.equals(InvoiceProgress.YES.getCode().toString(),invoiceProgress)){
                return  r.failure(101,"发票进度选择错误");
            }
            Map m =new HashMap();
            m.put("id",id);
            m.put("invoice_progress",invoiceProgress);
            m.put("invoice_progress_remarks",invoiceProgressRemarks);
            int result =ordersMapper.editInvoiceProgressById(m);
            if(result >0){
                return r.success("修改发票进度状态成功");
            }else{
                return r.failure(1,"修改发票进度失败");
            }
        }catch (Exception e){
            logger.error("修改发票进度失败请联系管理员",e);
            return r.failure(1,"修改发票进度失败请联系管理员");
        }
    }

    /**
     * 修改收款进度
     * @param request
     * @return
     */
    @RequestMapping(value = "/editPaymentProgress",method = RequestMethod.POST)
    public @ResponseBody
    Result editPaymentProgress(HttpServletRequest request){
        Result r = new Result();
        try {
            String id = request.getParameter("id");
            String paymentProgress = request.getParameter("paymentProgress");
            String paymentMethod = request.getParameter("paymentMethod");
            String paymentProgressRemarks = request.getParameter("paymentProgressRemarks");

            if(StringUtils.isBlank(id) || StringUtils.isBlank(paymentProgress) || StringUtils.isBlank(paymentProgressRemarks) ){
                return  r.failure(101,"参数错误");
            }
            if(!StringUtils.isNumeric(id)){
                return  r.failure(101,"订单错误");
            }
            if(!StringUtils.equals(PaymentProgress.NOTYET.getCode().toString(),paymentProgress) && !StringUtils.equals(PaymentProgress.YES.getCode().toString(),paymentProgress)){
                return  r.failure(101,"收款进度选择错误");
            }
            Map m =new HashMap();
            m.put("id",id);
            m.put("payment_progress",paymentProgress);
            m.put("payment_progress_remarks",paymentProgressRemarks);
            if(StringUtils.isBlank(paymentMethod)){
                m.put("payment_method",null);
            }else{
                m.put("payment_method",paymentMethod);
            }

            int result =ordersMapper.editPayMentProgressById(m);
            if(result >0){
                return r.success("修改收款进度成功");
            }else{
                return r.failure(1,"修改收款进度失败");
            }
        }catch (Exception e){
            logger.error("修改收款进度失败请联系管理员",e);
            return r.failure(1,"修改收款进度失败请联系管理员");
        }
    }

    /**
     *添加订单发货记录
     * @param
     * @param request
     * @return
     */
    @RequestMapping(value = "/addOrderDeliverRecords",method = RequestMethod.POST)
    public @ResponseBody
    Result addOrderDeliverRecords(OrderDeliverRecords odr, HttpServletRequest request){
        Date d = new Date();
        Result r = new Result();
        try {
            if(odr.getOrderId() == null){
                return r.failure(101,"订单号不能为空");
            }
            if(odr.getDeliverNumber() == null){
                return r.failure(101,"发货数量不能为空");
            }
            odr.setCreateTime(d);
            odr.setModifyTime(d);
            odr.setCreateUserId(commonService.getLoginUserId(request));
            int result = ordersDeliverRecordsMapper.insert(odr);
            if(result >0){
                return r.success("创建发货记录成功");
            }else{
                return r.failure(1,"创建发货记录失败");
            }
        }catch (Exception e){
            logger.error("创建发货记录失败请联系管理员",e);
            return r.failure(1,"创建发货记录失败请联系管理员");
        }
    }

    /**
     *删除订单发货记录
     * @param
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteOrderDeliverRecordsById",method = RequestMethod.POST)
    public @ResponseBody
    Result deleteOrderDeliverRecordsById(HttpServletRequest request){
        Result r = new Result();
        try {
            String  id =request.getParameter("id");
            if(StringUtils.isBlank(id) || !StringUtils.isNumeric(id)){
                return  r.failure(101,"参数错误");
            }
            int result = ordersDeliverRecordsMapper.deleteById(Long.valueOf(id));
            if(result > 0){
                return r.success("删除订单发货记录成功");
            }else{
                return r.failure(1,"删除订单发货记录失败");
            }
        }catch (Exception e){
            logger.error("删除订单发货记录失败请联系管理员",e);
            return r.failure(1,"删除订单发货记录失败请联系管理员");
        }
    }

    /**
     *根据用户查询订单列表
     * @param
     * @param request
     * @return
     */
    @RequestMapping(value = "/getOrdersPageByUserId",method = RequestMethod.POST)
    public @ResponseBody
    Result getOrderListByUserId(@Param("oq") OrderQuery oq,HttpServletRequest request){
        Result r = new Result();
        try {
            if(oq.getUserId() == null){
                return r.failure(101,"参数错误");
            }
            IPage<Orders> page = new Page<>(oq.getCurrent(),oq.getSize());

            page.setRecords(ordersMapper.selectOrdersListPage(page,oq));
            return r.success("获得用户的订单成功").setData(page);
        }catch (Exception e){
            logger.error("获得用户的订单失败请联系管理员",e);
            return r.failure(1,"获得用户的订单失败请联系管理员");
        }
    }



}
