package com.cq.start.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cq.start.SuperMapper;
import com.cq.start.domain.OrderDeliverRecords;
import com.cq.start.domain.OrderPaymentRecords;
import com.cq.start.domain.Orders;
import com.cq.start.domain.SampleUserRelate;
import com.cq.start.domain.querydomain.OrderQuery;
import javafx.scene.control.Pagination;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrdersMapper extends SuperMapper<Orders> {
    /**
     * 根据订单id 获得详情
     * @param id
     * @return
     */
    Orders getDetailById(Long id);

    /**
     * 分页查询订单列表
     * @param page
     * @param oq
     * @return
     */
    List<Orders> selectOrdersListPage(IPage page,@Param("oq") OrderQuery oq);

    /**
     * 修改订单发货进度
     * @param m
     * @return
     */
    int editDeliverProgressById(Map m);

    /**
     * 修改订单发票进度
     * @param m
     * @return
     */
    int editInvoiceProgressById(Map m);
    /**
     * 修改订单支付进度
     * @param m
     * @return
     */
    int editPayMentProgressById(Map m);

    /**
     * 根据订单查询有效的订单发货记录
     * @param oderId
     * @return
     */
    List<OrderDeliverRecords> getOrderDeliverRecordsByOrderId(Long oderId);
    /**
     * 根据订单查询有效的订单收款记录
     * @param oderId
     * @return
     */
    List<OrderPaymentRecords> getOrderPaymentRecordsByOrderId(Long oderId);


}
