package com.cq.start.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.cq.start.domain.LoginTickets;
import com.cq.start.domain.SampleUserRelate;
import com.cq.start.domain.Samples;
import com.cq.start.domain.User;
import com.cq.start.mapper.LoginTicketsMapper;
import com.cq.start.mapper.SampleUserRelateMapper;
import com.cq.start.mapper.SamplesMapper;
import com.cq.start.mapper.SystemUserMapper;
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
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/samples")
public class SamplesController extends BaseController {
    @Resource
    private SystemUserMapper systemUserMapper;
    @Resource
    private SamplesMapper samplesMapper;
    @Resource
    private SampleUserRelateMapper sampleUserRelateMapper;
    @Resource
    private LoginTicketsMapper loginTicketsMapper;
    @Autowired
    private CommonService commonService;


    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public @ResponseBody
    Result registerUser(Samples s, HttpServletRequest request){
        Result r = new Result();
        QueryWrapper<Samples> qw = new QueryWrapper<>();
        Date createTime =new Date();
        try {
            if(StringUtils.isBlank(s.getNumber())){
                return r.failure(101,"物料号不能为空");
            }

            if(s.getType() == null){
                return r.failure(101,"产品类型出错");
            }
            qw.eq("number",s.getNumber());
            Samples existence = samplesMapper.selectOne(qw);
            if(existence != null){
                return r.failure(101,"物料编号已存在，请修改");
            }

            s.setCreateUserId(commonService.getLoginUserId(request));
            s.setCreateTime(createTime);
            s.setModifyTime(createTime);
            int result = samplesMapper.insert(s);
            if(result >0){
                if(CollectionUtils.isNotEmpty(s.getUserIds())){
                    for(Integer userId :s.getUserIds()){
                        SampleUserRelate sur = new SampleUserRelate();
                        sur.setSampleId(s.getId());
                        sur.setUserId(userId);
                        sur.setCreateTime(createTime);
                        sur.setModifyTime(createTime);
                        sampleUserRelateMapper.insert(sur);
                    }
                }
                return r.success("添加产品成功");
            }else{
                return r.failure(1,"添加产品成功");
            }
        }catch (Exception e){
            logger.error("添加产品失败，请联系管理员",e);
            return r.failure(1,"添加产品失败，请联系管理员");
        }
    }

}
