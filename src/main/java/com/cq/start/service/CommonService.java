package com.cq.start.service;

import javax.servlet.http.HttpServletRequest;

public interface CommonService {

    /**
     * 获得当前登陆者的id
     * @param request
     * @return
     * @throws Exception
     */
    Long getLoginUserId(HttpServletRequest request) throws  Exception;

    /**
     * 生成订单号
     * @return
     */
    String createOrderCode();
}
