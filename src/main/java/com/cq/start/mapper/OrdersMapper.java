package com.cq.start.mapper;

import com.cq.start.SuperMapper;
import com.cq.start.domain.Orders;
import com.cq.start.domain.SampleUserRelate;

public interface OrdersMapper extends SuperMapper<Orders> {
    /**
     * 根据订单id 获得详情
     * @param id
     * @return
     */
    Orders getDetailById(Long id);
}
