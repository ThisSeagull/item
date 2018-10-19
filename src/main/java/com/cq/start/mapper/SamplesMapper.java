package com.cq.start.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cq.start.SuperMapper;
import com.cq.start.domain.Samples;

public interface SamplesMapper extends SuperMapper<Samples> {

    int setEnableById(Long id);
}
