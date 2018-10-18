package com.cq.start.service;

import javax.servlet.http.HttpServletRequest;

public interface CommonService {

    long getLoginUserId(HttpServletRequest request) throws  Exception;
}
