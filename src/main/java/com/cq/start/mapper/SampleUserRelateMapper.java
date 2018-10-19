package com.cq.start.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cq.start.SuperMapper;
import com.cq.start.domain.SampleUserRelate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface SampleUserRelateMapper extends SuperMapper<SampleUserRelate> {

    List<SampleUserRelate> getRelateUserBySampleId(Long sampleId);

}
