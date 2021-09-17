package com.demo.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.common.model.enums.DeleteStatusEnum;
import com.demo.common.utils.PinyinUtils;
import com.demo.domain.model.sys.dto.TSysParamDto;
import com.demo.domain.model.sys.entity.TSysParam;
import com.demo.domain.model.sys.vo.TSysParamVO;
import com.demo.sys.dao.TSysParamMapper;
import com.demo.sys.service.ITSysParamService;
import org.apache.ibatis.annotations.Param;
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
public class TSysParamServiceImpl extends ServiceImpl<TSysParamMapper, TSysParam> implements ITSysParamService {
    @Resource
    private TSysParamMapper tSysParamMapper;

    @Override
    public int save(TSysParamVO tSysParamVO) {
        TSysParam tSysParam = new TSysParam();
        BeanUtils.copyProperties(tSysParamVO, tSysParam);
        tSysParam.setInputCode(PinyinUtils.getPinYinHeadChar(tSysParam.getSpName()));
        return tSysParamMapper.insert(tSysParam);
    }

    @Override
    public int deleteByModel(TSysParamVO tSysParamVO) {
        TSysParam tSysParam = new TSysParam();
        BeanUtils.copyProperties(tSysParamVO, tSysParam);
        return tSysParamMapper.delete(Wrappers.<TSysParam>lambdaUpdate(tSysParam));
    }

    @Override
    public int update(TSysParamVO tSysParamVO) {
        TSysParam tSysParam = new TSysParam();
        BeanUtils.copyProperties(tSysParamVO, tSysParam);
        return tSysParamMapper.updateById(tSysParam);
    }

    @Override
    public TSysParam findByModel(TSysParamVO tSysParamVO) {
        TSysParam tSysParam = new TSysParam();
        BeanUtils.copyProperties(tSysParamVO, tSysParam);
        tSysParam.setDelFlag(DeleteStatusEnum.NO_ENUM.getCode());
        return tSysParamMapper.selectOne(Wrappers.<TSysParam>lambdaQuery(tSysParam));
    }

    @Override
    public List<TSysParam> findModelList(TSysParamVO tSysParamVO) {
        TSysParam tSysParam = new TSysParam();
        BeanUtils.copyProperties(tSysParamVO, tSysParam);
        return tSysParamMapper.selectList(Wrappers.<TSysParam>lambdaQuery(tSysParam));
    }

    @Override
    public IPage<TSysParam> selectByPage(TSysParamVO tSysParamVO, Integer page, Integer size) {
        IPage<TSysParam> iPage = new Page<>(page, size);
        TSysParam tSysParam = new TSysParam();
        BeanUtils.copyProperties(tSysParamVO, tSysParam);
        return tSysParamMapper.selectPage(iPage, Wrappers.<TSysParam>lambdaQuery(tSysParam));
    }

    @Override
    public IPage<TSysParamDto> selectDtoByPage(TSysParamVO tSysParamVO, Integer page, Integer size) {
        IPage<TSysParamVO> iPage = new Page<>(page, size);
        return tSysParamMapper.findDtoModelList(iPage,tSysParamVO);
    }
}
