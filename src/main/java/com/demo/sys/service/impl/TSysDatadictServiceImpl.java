package com.demo.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.common.model.enums.DeleteStatusEnum;
import com.demo.common.utils.PinyinUtils;
import com.demo.domain.model.sys.entity.TSysDatadict;
import com.demo.domain.model.sys.vo.TSysDatadictVO;
import com.demo.sys.dao.TSysDatadictMapper;
import com.demo.sys.service.ITSysDatadictService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by jky on 2021/08/10.
 */
@Service
@Transactional
public class TSysDatadictServiceImpl extends ServiceImpl<TSysDatadictMapper, TSysDatadict> implements ITSysDatadictService {
    @Resource
    private TSysDatadictMapper tSysDatadictMapper;

    @Override
    public int save(TSysDatadictVO tSysDatadictVO) {
        TSysDatadict tSysDatadict = new TSysDatadict();
        BeanUtils.copyProperties(tSysDatadictVO,tSysDatadict);
        tSysDatadict.setInputCode(PinyinUtils.getPinYinHeadChar(tSysDatadict.getDisplay()));
        return tSysDatadictMapper.insert(tSysDatadict);
    }

    @Override
    public int deleteByModel(TSysDatadictVO tSysDatadictVO) {
        TSysDatadict tSysDatadict = new TSysDatadict();
        BeanUtils.copyProperties(tSysDatadictVO,tSysDatadict);
        return tSysDatadictMapper.delete(Wrappers.<TSysDatadict>lambdaUpdate(tSysDatadict));
    }

    @Override
    public int update(TSysDatadictVO tSysDatadictVO) {
        TSysDatadict tSysDatadict = new TSysDatadict();
        BeanUtils.copyProperties(tSysDatadictVO,tSysDatadict);
        return tSysDatadictMapper.updateById(tSysDatadict);
    }

    @Override
    public TSysDatadict findByModel(TSysDatadictVO tSysDatadictVO) {
        TSysDatadict tSysDatadict = new TSysDatadict();
        BeanUtils.copyProperties(tSysDatadictVO,tSysDatadict);
        tSysDatadict.setDelFlag(DeleteStatusEnum.NO_ENUM.getCode());
        return tSysDatadictMapper.selectOne(Wrappers.<TSysDatadict>lambdaQuery(tSysDatadict));
    }

    @Override
    public List<TSysDatadict> findModelList(TSysDatadictVO tSysDatadictVO) {
        TSysDatadict tSysDatadict = new TSysDatadict();
        BeanUtils.copyProperties(tSysDatadictVO,tSysDatadict);
        return tSysDatadictMapper.findModelList(tSysDatadict);
    }

    @Override
    public IPage<TSysDatadict> selectByPage(TSysDatadictVO tSysDatadictVO, Integer page, Integer size) {
        IPage<TSysDatadict> iPage = new Page<>(page,size);
        TSysDatadict tSysDatadict = new TSysDatadict();
        BeanUtils.copyProperties(tSysDatadictVO,tSysDatadict);
        return tSysDatadictMapper.findModelList(iPage,tSysDatadict);
    }
}
