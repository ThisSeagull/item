package com.cq.start.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cq.start.domain.SystemUser;
import com.cq.start.domain.User;

public interface SystemUserMapper extends BaseMapper<SystemUser> {

    SystemUser selectByIdIgnoreStatus(Long id);
}
