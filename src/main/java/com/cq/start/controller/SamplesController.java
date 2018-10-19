package com.cq.start.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cq.start.domain.LoginTickets;
import com.cq.start.domain.SampleUserRelate;
import com.cq.start.domain.Samples;
import com.cq.start.domain.User;
import com.cq.start.domain.enums.Status;
import com.cq.start.domain.querydomain.SampleQuery;
import com.cq.start.domain.querydomain.UserQuery;
import com.cq.start.mapper.*;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
                    for(Long userId :s.getUserIds()){
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
    @RequestMapping(value = "/getSampleById")
    public @ResponseBody Result getSampleById(HttpServletRequest request){
        Result r = new Result();
        try {
            String id =request.getParameter("id");
            if(!StringUtils.isNumeric(id)){
                return r.failure(101,"参数错误");
            }
            QueryWrapper<Samples> qw =new QueryWrapper<>();
            qw.eq("id",id);
            Samples sample =  samplesMapper.selectOne(qw);
            return r.success("获得产品成功").setData(sample);

        }catch (Exception e){
            logger.error("获得产品失败请联系管理员",e);
            return r.failure(1,"获得产品失败请联系管理员");
        }
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public @ResponseBody Result getSamplesList(SampleQuery sq , HttpServletRequest request){
        Result r = new Result();
        try {
            QueryWrapper<Samples> qw  = new QueryWrapper<>();
            IPage<Samples> u = new Page<>(sq.getCurrent(),sq.getSize());

            IPage<Samples> list =  samplesMapper.selectPage(u,qw);
            return r.success("获得产品列表成功").setData(list);

        }catch (Exception e){
            logger.error("获得产品列表失败请联系管理员",e);
            return r.failure(1,"获得产品列表失败请联系管理员");
        }
    }

    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public @ResponseBody Result samplesList(SampleQuery sq , HttpServletRequest request){
        Result r = new Result();
        try {
            QueryWrapper<Samples> qw  = new QueryWrapper<>();
            IPage<Samples> u = new Page<>(sq.getCurrent(),sq.getSize());

            if(StringUtils.isNotBlank(sq.getSampleNumber())){
                qw.like("number",sq.getSampleNumber());
            }
            IPage<Samples> list =  samplesMapper.selectPage(u,qw);
            return r.success("获得产品列表成功").setData(list);

        }catch (Exception e){
            logger.error("获得产品列表失败请联系管理员",e);
            return r.failure(1,"获得产品列表失败请联系管理员");
        }
    }

    @RequestMapping(value = "/editSample",method = RequestMethod.POST)
    public @ResponseBody Result editUser(Samples s,HttpServletRequest request){
        Result r = new Result();
        QueryWrapper<Samples> qw = new QueryWrapper<>();
        Date nowTime =new Date();
        try {
            if(StringUtils.isBlank(s.getNumber())){
                return r.failure(101,"物料号不能为空");
            }
            if(s.getType() == null){
                return r.failure(101,"产品类型出错");
            }
            Samples oldSample = samplesMapper.selectById(s.getId());
            if(oldSample != null && !StringUtils.equals(oldSample.getNumber(),s.getNumber())){
                qw.eq("number",s.getNumber());
                Samples existence = samplesMapper.selectOne(qw);
                if(existence != null){
                    return r.failure(101,"物料编号已存在，请修改");
                }
            }
            s.setModifyTime(nowTime);
            int result = samplesMapper.updateById(s);
            if(result > 0){
                if(CollectionUtils.isNotEmpty(s.getUserIds())){
                    List<Long> batchNeedDeleteId = new ArrayList<>();
                    QueryWrapper<SampleUserRelate> customaryQw = new QueryWrapper<>();
                    customaryQw.eq("sample_id",s.getId());
                    List<SampleUserRelate> customaryList = sampleUserRelateMapper.selectList(customaryQw);
                    if(CollectionUtils.isNotEmpty(customaryList)){
                        for(SampleUserRelate oldSur:customaryList){
                           if(!s.getUserIds().contains(oldSur.getUserId())){
                               batchNeedDeleteId.add(oldSur.getId());
                           }
                        }
                    }
                    if(CollectionUtils.isNotEmpty(batchNeedDeleteId)){
                        sampleUserRelateMapper.deleteBatchIds(batchNeedDeleteId);
                    }
                    for(Long userId :s.getUserIds()){
                        QueryWrapper<SampleUserRelate> sur = new QueryWrapper<>();
                        sur.eq("sample_id",s.getId());
                        sur.eq("user_id",userId);
                        SampleUserRelate existence = sampleUserRelateMapper.selectOne(sur);
                        if(existence==null){//如果已经存在了对应关系，看是否被停用,如果被停用 那就重新启用
                            SampleUserRelate newSur = new SampleUserRelate();
                            newSur.setCreateTime(nowTime);
                            newSur.setModifyTime(nowTime);
                            newSur.setStatus(Status.Enable.getCode());
                            newSur.setSampleId(s.getId());
                            newSur.setUserId(userId);
                            sampleUserRelateMapper.insert(newSur);
                        }
                    }
                }else{
                    QueryWrapper<SampleUserRelate> delete = new QueryWrapper<>();
                    delete.eq("sample_id",s.getId());
                    sampleUserRelateMapper.delete(delete);
                }
                return r.success("修改产品信息成功");
            }else{
                return r.failure(101,"物料编号已存在，请修改");
            }
        }catch (Exception e){
            logger.error("修改产品信息失败请联系管理员",e);
            return r.failure(1,"修改产品信息失败请联系管理员");
        }
    }

    /**
     * 启用或停用 产品
     * @param sample
     * @param request
     * @return
     */
    @RequestMapping(value = "/editSampleStatus",method = RequestMethod.POST)
    public @ResponseBody Result editUserStatus(Samples sample ,HttpServletRequest request){
        Result r = new Result();
        try {
            if(sample.getStatus()!= Status.Disabled.getCode() && sample.getStatus()!=Status.Enable.getCode()){
                return r.failure(101,"参数错误");
            }
            sample.setModifyTime(new Date());
            int result = 0;
            if(sample.getStatus() ==Status.Disabled.getCode()){
                 result =  samplesMapper.deleteById(sample.getId());
            }else{
                result = samplesMapper.setEnableById(sample.getId());
            }

            if(result == 1){
                return r.success("更新产品状态成功");
            }else{
                return r.failure(1,"更新产品状态失败,请联系管理员");
            }
        }catch (Exception e){
            logger.error("更新产品状态失败,请联系管理员",e);
            return r.failure(1,"更新产品状态失败，请联系管理员");
        }
    }

    /**
     * 根据订单id获得关联用户
     * @param
     * @param request
     * @return
     */
    @RequestMapping(value = "/getRelateUserBySampleId",method = RequestMethod.POST)
    public @ResponseBody Result getRelateUserBySampleId(HttpServletRequest request){
        Result r = new Result();
        try {
            String sampleId = request.getParameter("sampleId");
            if(StringUtils.isBlank(sampleId)){
                return r.failure(101,"参数错误");
            }
            List<SampleUserRelate> s = sampleUserRelateMapper.getRelateUserBySampleId(Long.valueOf(sampleId));
            return r.success("获得关联用户成功").setData(s);

        }catch (Exception e){
            logger.error("获得关联用户失败,请联系管理员",e);
            return r.failure(1,"获得关联用户失败，请联系管理员");
        }
    }






}
