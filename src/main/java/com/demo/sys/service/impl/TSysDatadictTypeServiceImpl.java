package com.demo.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.common.model.enums.DeleteStatusEnum;
import com.demo.common.utils.PinyinUtils;
import com.demo.domain.model.sys.entity.TSysDatadictType;
import com.demo.domain.model.sys.vo.TSysDatadictTypeVO;
import com.demo.sys.dao.TSysDatadictTypeMapper;
import com.demo.sys.service.ITSysDatadictTypeService;
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
public class TSysDatadictTypeServiceImpl extends ServiceImpl<TSysDatadictTypeMapper, TSysDatadictType> implements ITSysDatadictTypeService {
    @Resource
    private TSysDatadictTypeMapper tSysDatadictTypeMapper;

    @Override
    public int save(TSysDatadictTypeVO tSysDatadictTypeVO) {
        TSysDatadictType tSysDatadictType = new TSysDatadictType();
        BeanUtils.copyProperties(tSysDatadictTypeVO,tSysDatadictType);
        tSysDatadictType.setInputCode(PinyinUtils.getPinYinHeadChar(tSysDatadictType.getName()));
        return tSysDatadictTypeMapper.insert(tSysDatadictType);
    }

    @Override
    public int deleteByModel(TSysDatadictTypeVO tSysDatadictTypeVO) {
        TSysDatadictType tSysDatadictType = new TSysDatadictType();
        BeanUtils.copyProperties(tSysDatadictTypeVO,tSysDatadictType);
        return tSysDatadictTypeMapper.delete(Wrappers.<TSysDatadictType>lambdaUpdate(tSysDatadictType));
    }

    @Override
    public int update(TSysDatadictTypeVO tSysDatadictTypeVO) {
        TSysDatadictType tSysDatadictType = new TSysDatadictType();
        BeanUtils.copyProperties(tSysDatadictTypeVO,tSysDatadictType);
        return tSysDatadictTypeMapper.updateById(tSysDatadictType);
    }

    @Override
    public TSysDatadictType findByModel(TSysDatadictTypeVO tSysDatadictTypeVO) {
        TSysDatadictType tSysDatadictType = new TSysDatadictType();
        BeanUtils.copyProperties(tSysDatadictTypeVO,tSysDatadictType);
        tSysDatadictType.setDelFlag(DeleteStatusEnum.NO_ENUM.getCode());
        return tSysDatadictTypeMapper.selectOne(Wrappers.<TSysDatadictType>lambdaQuery(tSysDatadictType));
    }

    @Override
    public List<TSysDatadictType> findModelList(TSysDatadictTypeVO tSysDatadictTypeVO) {
        TSysDatadictType tSysDatadictType = new TSysDatadictType();
        BeanUtils.copyProperties(tSysDatadictTypeVO,tSysDatadictType);
        return tSysDatadictTypeMapper.findModelList(tSysDatadictType);
    }

    @Override
    public IPage<TSysDatadictType> selectByPage(TSysDatadictTypeVO tSysDatadictTypeVO, Integer page, Integer size) {
        IPage<TSysDatadictType> iPage = new Page<>(page,size);
        TSysDatadictType tSysDatadictType = new TSysDatadictType();
        BeanUtils.copyProperties(tSysDatadictTypeVO,tSysDatadictType);
        return tSysDatadictTypeMapper.findModelList(iPage, tSysDatadictType);
    }
}
