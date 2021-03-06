package com.cq.start.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cq.start.domain.Samples;
import com.cq.start.domain.User;

public interface UserMapper extends BaseMapper <User> {
    int setEnableById(Long id);

    User selectByIdIgnoreStatus(Long id);
}
