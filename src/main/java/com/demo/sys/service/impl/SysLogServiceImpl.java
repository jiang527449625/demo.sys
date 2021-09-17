package com.demo.sys.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.common.mybatis.Result;
import com.demo.domain.model.sys.entity.SysLog;
import com.demo.domain.model.sys.vo.SysLogVO;
import com.demo.domain.model.utils.UserSessionUtils;
import com.demo.sys.dao.SysLogMapper;
import com.demo.sys.service.ISysLogService;
import com.demo.sys.third.FeignEsClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by jky on 2021/04/28.
 */
@Service
@Transactional
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {
    @Resource
    private SysLogMapper sysLogMapper;
    @Resource
    private FeignEsClient feignEsClient;
    @Value("${log_es_index}")
    private String logEsIndex;
    @Value("${log_es_type}")
    private String logType;

    @Override
    public void save(SysLogVO sysLogVO) {
        sysLogVO.setCreateBy(UserSessionUtils.getCurrentUser().getId());
        sysLogVO.setCreateAt(new Date());
        saveEs(sysLogVO);
    }

    @Override
    public void deleteByModel(SysLogVO sysLogVO) {
        SysLog sysLog = new SysLog();
        BeanUtils.copyProperties(sysLogVO,sysLog);
        sysLogMapper.delete(Wrappers.<SysLog>lambdaQuery(sysLog));
    }

    @Override
    public void update(SysLogVO sysLogVO) {
        SysLog sysLog = new SysLog();
        BeanUtils.copyProperties(sysLogVO,sysLog);
        sysLogMapper.updateById(sysLog);
    }

    @Override
    public SysLog findByModel(SysLogVO sysLogVO) {
        SysLog sysLog = new SysLog();
        BeanUtils.copyProperties(sysLogVO,sysLog);
        return sysLogMapper.selectOne(Wrappers.<SysLog>lambdaQuery(sysLog));
    }

    @Override
    public List<SysLog> findModelList(SysLogVO sysLogVO) {
        SysLog sysLog = new SysLog();
        BeanUtils.copyProperties(sysLogVO,sysLog);
        return sysLogMapper.selectList(Wrappers.<SysLog>lambdaQuery(sysLog));
    }

    @Override
    public IPage<SysLog> selectByPage(SysLogVO sysLogVO, Integer page, Integer size) {
        IPage<SysLog> iPage = new Page<>(page,size);
        SysLog sysLog = new SysLog();
        BeanUtils.copyProperties(sysLogVO,sysLog);
        return sysLogMapper.selectPage(iPage,Wrappers.<SysLog>lambdaQuery(sysLog));
    }

    @Override
    public Result saveEs(SysLogVO sysLogVO) {
        List<SysLogVO> list = new ArrayList<>();
        list.add(sysLogVO);
        JSONArray array = JSONArray.parseArray(JSONArray.toJSONString(list));
        return feignEsClient.saveAdsStatisticsCount(logEsIndex,logType,array);
    }

    @Override
    public List<Map> getTableNameByList(String tableName, String id) {
        return sysLogMapper.selectTableNameByList(tableName, id);
    }
}
